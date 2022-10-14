package ru.practicum.ewm.requests.model.dto;

import ru.practicum.ewm.requests.model.Request;

public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getUser().getId())
                .status(request.getStatus())
                .build();
    }
}