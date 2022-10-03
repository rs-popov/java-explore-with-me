package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ForbiddenAccessException;
import ru.practicum.ewm.exceptions.ObjectNotFoundException;
import ru.practicum.ewm.requests.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import javax.validation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public EventOutputDto getPublishedEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + id + " was not found."));
        if (event.getState().equals(EventState.PUBLISHED)) {
            return getFullOutputDto(event);
        } else {
            throw new ObjectNotFoundException("Event with id=" + id + " was not published.");
        }
    }

    @Override
    @Transactional
    public EventOutputDto createEvent(Long userId, EventInputDto eventInputDto) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with id=" + userId + " was not found."));
        Category category = categoryRepository.findById(eventInputDto.getCategory())
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + eventInputDto.getCategory() + " was not found."));
        Event event = EventMapper.toEventFromInputDto(eventInputDto, category, initiator);
        validateEventDate(event, 2);
        return getFullOutputDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public List<EventOutputShortDto> getEventsByInitiator(Long userId, Integer from, Integer size) {
        return eventRepository.getEventsByInitiator(userId, getPageRequest(from, size)).stream()
                .map(this::getShortOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventOutputDto updateEvent(Long userId, EventUpdateDto update) {
        Event oldEvent = eventRepository.findById(update.getEventId())
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + update.getEventId() + " was not found."));
        if (!Objects.equals(oldEvent.getInitiator().getId(), userId)) {
            throw new ForbiddenAccessException("Only initiator can update event.");
        }
        if (!(oldEvent.getState().equals(EventState.PENDING) || oldEvent.getState().equals(EventState.CANCELED))) {
            throw new ForbiddenAccessException("Only pending or canceled events can be changed.");
        }
        EventInputDto eventInputDto = EventMapper.toEventInputDtoFromUpdate(update, oldEvent);
        validateInputDto(eventInputDto);
        User initiator = oldEvent.getInitiator();
        Category category = categoryRepository.findById(eventInputDto.getCategory())
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + eventInputDto.getCategory() + " was not found."));
        Event event = EventMapper.toEventFromInputDto(eventInputDto, category, initiator);
        validateEventDate(event, 2);
        event.setId(update.getEventId());
        return getFullOutputDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventOutputDto getEventByInitiator(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + " was not found."));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenAccessException("Only initiator can get full info about event.");
        }
        return getFullOutputDto(event);
    }

    @Override
    @Transactional
    public EventOutputDto rejectEventByInitiator(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + " was not found."));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenAccessException("Only initiator or admin can reject event.");
        }
        if (!(event.getState().equals(EventState.PENDING))) {
            throw new ForbiddenAccessException("Only pending or canceled events can be changed.");
        }
        event.setState(EventState.CANCELED);
        return getFullOutputDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventOutputDto publishEventByAdmin(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + " was not found."));
        validateEventDate(event, 1);
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ForbiddenAccessException("Only pending events can be published");
        }
        event.setState(EventState.PUBLISHED);
        return getFullOutputDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventOutputDto rejectEventByAdmin(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + " was not found."));
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenAccessException("Only pending or canceled events can be reject");
        }
        event.setState(EventState.CANCELED);
        return getFullOutputDto(eventRepository.save(event));
    }

    @Override
    public List<EventOutputShortDto> getEvents(String text,
                                               List<Long> categories,
                                               Boolean paid,
                                               String rangeStart,
                                               String rangeEnd,
                                               Boolean onlyAvailable,
                                               String sort,
                                               Integer from,
                                               Integer size) {
        List<Event> events = eventRepository.getEvents(text, categories, paid, getDate(rangeStart),
                        getDate(rangeEnd), getPageRequest(from, size))
                .stream()
                .collect(Collectors.toList());
        if (onlyAvailable != null && onlyAvailable) {
            events = events.stream()
                    .filter(event -> event.getParticipantLimit() == 0)
                    .filter(event -> event.getParticipantLimit() > requestRepository.getCountConfirmedRequestByEventId(event.getId()))
                    .collect(Collectors.toList());
        }
        if (sort != null) {
            switch (sort) {
                case "EVENT_DATE":
                    events = events.stream().sorted(Comparator.comparing(Event::getEventDate))
                            .collect(Collectors.toList());
                    break;
                case "VIEWS":
//TODO
                    break;
                default: {
                    throw new BadRequestException("No such sort type:" + sort);
                }
            }
        }
        return events.stream().map(this::getShortOutputDto).collect(Collectors.toList());
    }

    @Override
    public List<EventOutputDto> searchEvents(List<Long> users,
                                             List<EventState> states,
                                             List<Long> categories,
                                             String rangeStart,
                                             String rangeEnd,
                                             Integer from,
                                             Integer size) {
        List<Event> events = eventRepository.searchEvents(users,
                        states,
                        categories,
                        getDate(rangeStart),
                        getDate(rangeEnd),
                        getPageRequest(from, size))
                .stream()
                .collect(Collectors.toList());
        return events.stream().
                map(this::getFullOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventOutputDto updateEventByAdmin(Long eventId, EventInputDto update) {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + " was not found."));
        Event event = Event.builder()
                .annotation(update.getAnnotation() != null ? update.getAnnotation() : oldEvent.getAnnotation())
                .category(update.getCategory() != null ? categoryRepository.findById(update.getCategory()).get() : oldEvent.getCategory())
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
        long views = 0; //TODO
        return EventMapper.toEventOutputDto(event, requests, views);
    }

    private EventOutputShortDto getShortOutputDto(Event event) {
        long requests = requestRepository.getCountConfirmedRequestByEventId(event.getId());
        long views = 0; //TODO
        return EventMapper.toEventOutputShortDto(event, requests, views);
    }

    private void validateEventDate(Event event, int hoursBeforeStart) {
        if (ChronoUnit.HOURS.between(LocalDateTime.now(), event.getEventDate()) < hoursBeforeStart) {
            throw new BadRequestException("Event data cannot be earlier than " + hoursBeforeStart + " hours from the current moment.");
        }
    }

    private LocalDateTime getDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        if (date != null) {
            return LocalDateTime.parse(date, formatter);
        }
        return null;
    }
}
