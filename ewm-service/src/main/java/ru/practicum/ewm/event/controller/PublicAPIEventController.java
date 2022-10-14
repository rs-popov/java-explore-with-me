package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.model.dto.EventOutputDto;
import ru.practicum.ewm.event.model.dto.EventOutputShortDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.statistics.aop.CreatingHit;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicAPIEventController {
    private final EventService eventService;

    /**
     * Получение событий с возможностью фильтрации
     *
     * @param text          - текст для поиска в содержимом аннотации и подробном описании события
     * @param categories    - список идентификаторов категорий в которых будет вестись поиск
     * @param paid          - поиск только платных/бесплатных событий
     * @param rangeStart    - дата и время не раньше которых должно произойти событие
     * @param rangeEnd      - дата и время не позже которых должно произойти событие
     * @param onlyAvailable - только события у которых не исчерпан лимит запросов на участие
     * @param sort          - Вариант сортировки: по дате события или по количеству просмотров
     * @param from          - количество событий, которые нужно пропустить для формирования текущего набора
     * @param size          - количество событий в наборе
     */
    @GetMapping()
    @CreatingHit
    public List<EventOutputShortDto> getEventsByLoc(HttpServletRequest request,
                                                    @RequestParam(required = false) String text,
                                                    @RequestParam(required = false) List<Long> categories,
                                                    @RequestParam(required = false) Boolean paid,
                                                    @RequestParam(required = false) String rangeStart,
                                                    @RequestParam(required = false) String rangeEnd,
                                                    @RequestParam(required = false) Boolean onlyAvailable,
                                                    @RequestParam(required = false) String sort,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.searchEventsByUser(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     *
     * @param id - id события
     */
    @GetMapping("/{id}")
    @CreatingHit
    public EventOutputDto getPublishedEventById(HttpServletRequest request,
                                                @PathVariable Long id) {
        return eventService.getPublishedEventById(id);
    }

    /**
     * Получение опубликованных доступных событий, запланированных на области с центром lat, lon и радиусом distance
     */
    @GetMapping("/loc")
    @CreatingHit
    public List<EventOutputShortDto> getEventsByLoc(HttpServletRequest request,
                                                    @RequestParam Double lat,
                                                    @RequestParam Double lon,
                                                    @RequestParam Double distance) {
        return eventService.getEventsByLoc(lat, lon, distance);
    }
}