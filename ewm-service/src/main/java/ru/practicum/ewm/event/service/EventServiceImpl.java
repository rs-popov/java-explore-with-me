package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.repository.CategoryRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.dto.*;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ForbiddenAccessException;
import ru.practicum.ewm.exceptions.ObjectNotFoundException;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.repository.LocationRepository;
import ru.practicum.ewm.logging.CreationLogging;
import ru.practicum.ewm.logging.UpdateLogging;
import ru.practicum.ewm.requests.repository.RequestRepository;
import ru.practicum.ewm.statistics.client.StatisticsClient;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import javax.validation.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final StatisticsClient statisticsClient;

    @Override
    public EventOutputDto getPublishedEventById(Long id) {
        Event event = getEvent(id);
        if (event.getState().equals(EventState.PUBLISHED)) {
            return getFullOutputDto(event);
        } else {
            log.warn("Event with id={} was not published.", id);
            throw new ForbiddenAccessException("Event with id=" + id + " was not published.");
        }
    }

    @Override
    @Transactional
    @CreationLogging
    public EventOutputDto createEvent(Long userId, EventInputDto eventInputDto) {
        User initiator = getUser(userId);
        Category category = getCategory(eventInputDto.getCategory());
        Location location = getOrCreateLocation(eventInputDto);
        Event event = EventMapper.toEventFromInputDto(eventInputDto, category, initiator, location);
        validateEventDate(event, 2);
        return EventMapper.toEventOutputDto(eventRepository.save(event), 0L, 0L);
    }

    @Override
    public List<EventOutputShortDto> getEventsByInitiator(Long userId, Integer from, Integer size) {
        return eventRepository.getEventsByInitiator(userId, getPageRequest(from, size)).stream()
                .map(this::getShortOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @UpdateLogging
    public EventOutputDto updateEventByInitiator(Long userId, EventUpdateDto update) {
        Event oldEvent = getEvent(update.getEventId());

        if (!Objects.equals(oldEvent.getInitiator().getId(), userId)) {
            log.warn("Event id={} cannot update - Only initiator can update event.", update.getEventId());
            throw new ForbiddenAccessException("Only initiator can update event.");
        }
        if (!(oldEvent.getState().equals(EventState.PENDING) || oldEvent.getState().equals(EventState.CANCELED))) {
            log.warn("Event id={} cannot update - Only pending or canceled events can be changed.", update.getEventId());
            throw new ForbiddenAccessException("Only pending or canceled events can be changed.");
        }
        EventInputDto eventInputDto = EventMapper.toEventInputDtoFromUpdate(update, oldEvent);
        validateInputDto(eventInputDto);
        User initiator = oldEvent.getInitiator();
        Category category = getCategory(eventInputDto.getCategory());
        Location location = getOrCreateLocation(eventInputDto);
        Event event = EventMapper.toEventFromInputDto(eventInputDto, category, initiator, location);
        validateEventDate(event, 2);
        event.setId(update.getEventId());
        return getFullOutputDto(eventRepository.save(event));
    }

    @Override
    public EventOutputDto getEventByInitiator(Long userId, Long eventId) {
        Event event = getEvent(eventId);
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            log.warn("Only initiator can get full info about event id={}", eventId);
            throw new ForbiddenAccessException("Only initiator can get full info about event.");
        }
        return getFullOutputDto(event);
    }

    @Override
    @Transactional
    @UpdateLogging
    public EventOutputDto rejectEventByInitiator(Long userId, Long eventId) {
        Event event = getEvent(eventId);
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            log.warn("Only initiator or admin can reject event id={}", eventId);
            throw new ForbiddenAccessException("Only initiator or admin can reject event.");
        }
        if (!(event.getState().equals(EventState.PENDING))) {
            log.warn("Event id={} cannot be rejected - Only pending events can be canceled.", eventId);
            throw new ForbiddenAccessException("Only pending events can be canceled.");
        }
        event.setState(EventState.CANCELED);
        return getFullOutputDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    @UpdateLogging
    public EventOutputDto publishEventByAdmin(Long eventId) {
        Event event = getEvent(eventId);
        validateEventDate(event, 1);
        if (!event.getState().equals(EventState.PENDING)) {
            log.warn("Event id={} cannot be published - Only pending events can be published.", eventId);
            throw new ForbiddenAccessException("Only pending events can be published.");
        }
        event.setState(EventState.PUBLISHED);
        return getFullOutputDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    @UpdateLogging
    public EventOutputDto rejectEventByAdmin(Long eventId) {
        Event event = getEvent(eventId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            log.warn("Event id={} cannot be rejected - Published events can be changed.", eventId);
            throw new ForbiddenAccessException("Published events can be reject");
        }
        event.setState(EventState.CANCELED);
        return getFullOutputDto(eventRepository.save(event));
    }

    @Override
    public List<EventOutputShortDto> searchEventsByUser(String text,
                                                        List<Long> categories,
                                                        Boolean paid,
                                                        String rangeStart,
                                                        String rangeEnd,
                                                        Boolean onlyAvailable,
                                                        String sort,
                                                        Integer from,
                                                        Integer size) {
        List<Event> events = eventRepository.getEvents(text,
                        categories,
                        paid,
                        rangeStart,
                        rangeEnd,
                        getPageRequest(from, size))
                .stream()
                .collect(Collectors.toList());

        if (onlyAvailable != null && onlyAvailable) {
            events = events.stream()
                    .filter(e -> e.getParticipantLimit() == 0)
                    .filter(e -> e.getParticipantLimit() > requestRepository.getCountConfirmedRequestByEventId(e.getId()))
                    .collect(Collectors.toList());
        }
        List<EventOutputShortDto> results = null;
        if (sort != null) {
            switch (sort) {
                case "EVENT_DATE":
                    results = events.stream()
                            .sorted(Comparator.comparing(Event::getEventDate))
                            .map(this::getShortOutputDto)
                            .collect(Collectors.toList());
                    break;
                case "VIEWS":
                    results = events.stream()
                            .map(this::getShortOutputDto)
                            .sorted(Comparator.comparing(EventOutputShortDto::getViews))
                            .collect(Collectors.toList());
                    break;
                default: {
                    throw new BadRequestException("No such sort type:" + sort);
                }
            }
        }
        return results;
    }

    @Override
    public List<EventOutputDto> searchEventsByAdmin(List<Long> users,
                                                    List<EventState> states,
                                                    List<Long> categories,
                                                    String rangeStart,
                                                    String rangeEnd,
                                                    Integer from,
                                                    Integer size) {
        List<Event> events = eventRepository.searchEvents(users,
                        states,
                        categories,
                        rangeStart,
                        rangeEnd,
                        getPageRequest(from, size))
                .stream()
                .collect(Collectors.toList());
        return events.stream()
                .map(this::getFullOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @UpdateLogging
    public EventOutputDto updateEventByAdmin(Long eventId, EventInputDto update) {
        Event oldEvent = getEvent(eventId);
        Event event = Event.builder()
                .annotation(update.getAnnotation() != null ? update.getAnnotation() : oldEvent.getAnnotation())
                .category(update.getCategory() != null ? getCategory(update.getCategory()) : oldEvent.getCategory())
                .description(update.getDescription() != null ? update.getDescription() : oldEvent.getDescription())
                .eventDate(update.getEventDate() != null ? update.getEventDate() : oldEvent.getEventDate())
                .state(oldEvent.getState())
                .requestModeration(update.getRequestModeration() != null ? update.getRequestModeration() : oldEvent.getRequestModeration())
                .id(oldEvent.getId())
                .createdOn(oldEvent.getCreatedOn())
                .title(update.getTitle() != null ? update.getTitle() : oldEvent.getTitle())
                .paid(update.getPaid() != null ? update.getPaid() : oldEvent.getPaid())
                .location(update.getLocation() != null ? update.getLocation() : oldEvent.getLocation())
                .initiator(oldEvent.getInitiator())
                .participantLimit(update.getParticipantLimit() != null ? update.getParticipantLimit() : oldEvent.getParticipantLimit())
                .publishedOn(oldEvent.getPublishedOn())
                .build();
        return getFullOutputDto(eventRepository.save(event));
    }

    @Override
    public List<EventOutputShortDto> getEventsByLoc(Double lat, Double lon, Double distance) {
        List<Location> locations = locationRepository.getLocations(lat, lon, distance);
        return eventRepository.searchEventsInLoc(locations).stream()
                .map(this::getShortOutputDto)
                .collect(Collectors.toList());
    }

    private void validateInputDto(EventInputDto eventInputDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EventInputDto>> violations = validator.validate(eventInputDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private PageRequest getPageRequest(Integer from, Integer size) {
        int page = from < size ? 0 : from / size;
        return PageRequest.of(page, size);
    }

    private EventOutputDto getFullOutputDto(Event event) {
        long requests = requestRepository.getCountConfirmedRequestByEventId(event.getId());
        long views = statisticsClient.getStatsForEvent(event.getId());
        return EventMapper.toEventOutputDto(event, requests, views);
    }

    private EventOutputShortDto getShortOutputDto(Event event) {
        long requests = requestRepository.getCountConfirmedRequestByEventId(event.getId());
        long views = statisticsClient.getStatsForEvent(event.getId());
        return EventMapper.toEventOutputShortDto(event, requests, views);
    }

    private void validateEventDate(Event event, int hoursBeforeStart) {
        if (ChronoUnit.HOURS.between(LocalDateTime.now(), event.getEventDate()) < hoursBeforeStart) {
            throw new BadRequestException("Event data cannot be earlier than " + hoursBeforeStart + " hours from the current moment.");
        }
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + " was not found."));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with id=" + userId + " was not found."));
    }

    private Category getCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + catId + " was not found."));
    }

    private Location getOrCreateLocation(EventInputDto eventInputDto) {
        Optional<Location> locationFromInput = locationRepository.findLocation(eventInputDto.getLocation().getLat(),
                eventInputDto.getLocation().getLon());
        Location resultLocation;
        if (locationFromInput.isPresent()) {
            resultLocation = locationFromInput.get();
        } else {
            resultLocation = locationRepository.save(eventInputDto.getLocation());
            log.info("New location id={} was created (lat={}, lon={}).",
                    resultLocation.getId(), resultLocation.getLat(), resultLocation.getLon());
        }
        return resultLocation;
    }
}