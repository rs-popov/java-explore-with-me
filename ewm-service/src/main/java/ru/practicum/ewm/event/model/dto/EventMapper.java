package ru.practicum.ewm.event.model.dto;

import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.model.LocationMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.dto.UserMapper;

import java.time.LocalDateTime;

public class EventMapper {

    public static Event toEventFromInputDto(EventInputDto eventInputDto, Category category, User initiator, Location location) {
        return Event.builder()
                .annotation(eventInputDto.getAnnotation())
                .category(category)
                .description(eventInputDto.getDescription())
                .eventDate(eventInputDto.getEventDate())
                .initiator(initiator)
                .location(location)
                .paid(eventInputDto.getPaid())
                .participantLimit(eventInputDto.getParticipantLimit())
                .title(eventInputDto.getTitle())
                .createdOn(LocalDateTime.now())
                .requestModeration(eventInputDto.getRequestModeration())
                .state(EventState.PENDING)
                .build();
    }

    public static EventOutputDto toEventOutputDto(Event event, Long request, Long views) {
        return EventOutputDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .confirmedRequests(request)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortOutputDto(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static EventOutputShortDto toEventOutputShortDto(Event event, Long request, Long views) {
        return EventOutputShortDto.builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .confirmedRequests(request)
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortOutputDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static EventInputDto toEventInputDtoFromUpdate(EventUpdateDto update, Event oldEvent) {
        return EventInputDto.builder()
                .annotation(update.getAnnotation() != null ? update.getAnnotation() : oldEvent.getAnnotation())
                .category(update.getCategory() != null ? update.getCategory() : oldEvent.getCategory().getId())
                .description(update.getDescription() != null ? update.getDescription() : oldEvent.getDescription())
                .eventDate(update.getEventDate() != null ? update.getEventDate() : oldEvent.getEventDate())
                .location(update.getLocation() != null ? LocationMapper.toLocation(update.getLocation()) : oldEvent.getLocation())
                .paid(update.getPaid() != null ? update.getPaid() : oldEvent.getPaid())
                .participantLimit(update.getParticipantLimit() != null ? update.getParticipantLimit() : oldEvent.getParticipantLimit())
                .title(update.getTitle() != null ? update.getTitle() : oldEvent.getTitle())
                .requestModeration(update.getRequestModeration() != null ? update.getRequestModeration() : oldEvent.getRequestModeration())
                .build();
    }
}