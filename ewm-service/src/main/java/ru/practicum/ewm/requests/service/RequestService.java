package ru.practicum.ewm.requests.service;

import ru.practicum.ewm.requests.dto.RequestDto;

import java.util.List;

public interface RequestService {

    List<RequestDto> getAllRequestByRequester(Long userId);

    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequestByRequester(Long userId, Long requestId);

    List<RequestDto> getRequestsByInitiator(Long userId, Long eventId);

    RequestDto confirmRequestByInitiator(Long userId, Long eventId, Long reqId);

    RequestDto rejectRequestByInitiator(Long userId, Long eventId, Long reqId);
}