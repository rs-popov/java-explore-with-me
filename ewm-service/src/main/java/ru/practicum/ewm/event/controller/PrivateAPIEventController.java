package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventInputDto;
import ru.practicum.ewm.event.dto.EventOutputDto;
import ru.practicum.ewm.event.dto.EventOutputShortDto;
import ru.practicum.ewm.event.dto.EventUpdateDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.requests.dto.RequestDto;
import ru.practicum.ewm.requests.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@CrossOrigin(origins = "*")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "users/{userId}/events")
public class PrivateAPIEventController {
    private final EventService eventService;
    private final RequestService requestService;

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
        return eventService.updateEvent(userId, eventUpdateDto);
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

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     *
     * @param userId  - id текущего пользователя
     * @param eventId - id события
     */
    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestByInitiator(@PathVariable Long userId,
                                                  @PathVariable Long eventId) {
        return requestService.getRequestsByInitiator(userId, eventId);
    }

    /**
     * Подтверждение чужой заявки на участие в событии текущего пользователя
     *
     * @param userId  - id текущего пользователя
     * @param eventId - id события
     * @param reqId   - id заявки, которую отменяет текущий пользователь
     */
    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequestByInitiator(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @PathVariable Long reqId) {
        return requestService.confirmRequestByInitiator(userId, eventId, reqId);
    }

    /**
     * Отклонение чужой заявки на участие в событии текущего пользователя
     *
     * @param userId  - id текущего пользователя
     * @param eventId - id события
     * @param reqId   - id заявки, которую отменяет текущий пользователь
     */
    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequestByInitiator(@PathVariable Long userId,
                                               @PathVariable Long eventId,
                                               @PathVariable Long reqId) {
        return requestService.rejectRequestByInitiator(userId, eventId, reqId);
    }
}