package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.model.dto.EventInputDto;
import ru.practicum.ewm.event.model.dto.EventOutputDto;
import ru.practicum.ewm.event.model.dto.EventOutputShortDto;
import ru.practicum.ewm.event.model.dto.EventUpdateDto;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class PrivateAPIEventController {
    private final EventService eventService;

    /**
     * Добавление нового события
     *
     * @param userId        - id текущего пользователя
     * @param eventInputDto - данные добавляемого события
     */
    @PostMapping()
    public EventOutputDto createEvent(@PathVariable Long userId,
                                      @RequestBody @Valid EventInputDto eventInputDto) {
        return eventService.createEvent(userId, eventInputDto);
    }

    /**
     * Получение событий, добавленных текущим пользователем
     *
     * @param userId - id текущего пользователя
     * @param from   - количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   - количество элементов в наборе
     */
    @GetMapping
    public List<EventOutputShortDto> getEventsByInitiator(@PathVariable Long userId,
                                                          @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                          @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.getEventsByInitiator(userId, from, size);
    }

    /**
     * Изменение события добавленного текущим пользователем
     *
     * @param userId         - id текущего пользователя
     * @param eventUpdateDto - Новые данные события
     */
    @PatchMapping
    public EventOutputDto updateEvent(@PathVariable Long userId,
                                      @RequestBody @Valid EventUpdateDto eventUpdateDto) {
        return eventService.updateEventByInitiator(userId, eventUpdateDto);
    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     *
     * @param userId  - id текущего пользователя
     * @param eventId - id события
     */
    @GetMapping("/{eventId}")
    public EventOutputDto getEventByInitiator(@PathVariable Long userId,
                                              @PathVariable Long eventId) {
        return eventService.getEventByInitiator(userId, eventId);
    }

    /**
     * Отмена события добавленного текущим пользователем.
     *
     * @param userId  - id текущего пользователя
     * @param eventId - id события
     */
    @PatchMapping("/{eventId}")
    public EventOutputDto rejectEventByInitiator(@PathVariable Long userId,
                                                 @PathVariable Long eventId) {
        return eventService.rejectEventByInitiator(userId, eventId);
    }
}