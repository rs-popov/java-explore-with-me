package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.dto.EventInputDto;
import ru.practicum.ewm.event.model.dto.EventOutputDto;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "admin/events")
public class AdminAPIEventController {
    private final EventService eventService;

    /**
     * Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
     *
     * @param users      - список id пользователей, чьи события нужно найти
     * @param states     - список состояний в которых находятся искомые события
     * @param categories - список id категорий в которых будет вестись поиск
     * @param rangeStart - дата и время не раньше которых должно произойти событие
     * @param rangeEnd   - дата и время не позже которых должно произойти событие
     * @param from       - количество событий, которые нужно пропустить для формирования текущего набора
     * @param size       - количество событий в наборе
     */
    @GetMapping
    public List<EventOutputDto> searchEvent(@RequestParam(required = false) List<Long> users,
                                            @RequestParam(required = false) List<EventState> states,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.searchEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /**
     * Редактирование данных любого события администратором. Валидация данных не требуется.
     *
     * @param eventId    - id события
     * @param inputEvent - Данные для изменения информации о событии
     */
    @PutMapping("/{eventId}")
    public EventOutputDto updateEventByAdmin(@PathVariable Long eventId,
                                             @RequestBody EventInputDto inputEvent) {
        return eventService.updateEventByAdmin(eventId, inputEvent);
    }

    /**
     * Публикация события
     *
     * @param eventId - id события
     */
    @PatchMapping("/{eventId}/publish")
    public EventOutputDto publishEventByAdmin(@PathVariable Long eventId) {
        return eventService.publishEventByAdmin(eventId);
    }

    /**
     * Отклонение события
     *
     * @param eventId - id события
     */
    @PatchMapping("/{eventId}/reject")
    public EventOutputDto rejectEventByAdmin(@PathVariable Long eventId) {
        return eventService.rejectEventByAdmin(eventId);
    }
}