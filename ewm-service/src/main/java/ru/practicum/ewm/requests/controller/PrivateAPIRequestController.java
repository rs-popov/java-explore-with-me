package ru.practicum.ewm.requests.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.requests.model.dto.RequestDto;
import ru.practicum.ewm.requests.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateAPIRequestController {
    private final RequestService requestService;

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     *
     * @param userId  - id текущего пользователя
     * @param eventId - id события
     */
    @PostMapping
    public RequestDto createRequest(@PathVariable Long userId,
                                    @RequestParam Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     *
     * @param userId - id текущего пользователя
     */
    @GetMapping
    public List<RequestDto> getAllRequestByRequester(@PathVariable Long userId) {
        return requestService.getAllRequestByRequester(userId);
    }

    /**
     * Отмена своего запроса на участие в событии
     *
     * @param userId    - id текущего пользователя
     * @param requestId - id запроса на участие
     */
    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId,
                                    @PathVariable Long requestId) {
        return requestService.cancelRequestByRequester(userId, requestId);
    }
}