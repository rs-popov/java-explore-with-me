package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.dto.EventInputDto;
import ru.practicum.ewm.event.model.dto.EventOutputDto;
import ru.practicum.ewm.event.model.dto.EventOutputShortDto;
import ru.practicum.ewm.event.model.dto.EventUpdateDto;

import java.util.List;

public interface EventService {
    EventOutputDto getPublishedEventById(Long id);

    EventOutputDto createEvent(Long userId,
                               EventInputDto eventInputDto);

    List<EventOutputShortDto> getEventsByInitiator(Long userId,
                                                   Integer from,
                                                   Integer size);

    EventOutputDto updateEventByInitiator(Long userId,
                                          EventUpdateDto eventUpdateDto);

    EventOutputDto getEventByInitiator(Long userId,
                                       Long eventId);

    EventOutputDto rejectEventByInitiator(Long userId,
                                          Long eventId);

    EventOutputDto publishEventByAdmin(Long eventId);

    EventOutputDto rejectEventByAdmin(Long eventId);

    List<EventOutputShortDto> searchEventsByUser(String text,
                                                 List<Long> categories,
                                                 Boolean paid,
                                                 String rangeStart,
                                                 String rangeEnd,
                                                 Boolean onlyAvailable,
                                                 String sort,
                                                 Integer from,
                                                 Integer size);

    List<EventOutputDto> searchEventsByAdmin(List<Long> users,
                                             List<EventState> states,
                                             List<Long> categories,
                                             String rangeStart,
                                             String rangeEnd,
                                             Integer from,
                                             Integer size);

    EventOutputDto updateEventByAdmin(Long eventId,
                                      EventInputDto inputEvent);

    List<EventOutputShortDto> getEventsByLoc(Double lat,
                                             Double lon,
                                             Double distance);
}