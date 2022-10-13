package ru.practicum.ewm.requests.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.requests.model.dto.RequestDto;
import ru.practicum.ewm.requests.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class InitiatorAPIRequestController {
    private final RequestService requestService;

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