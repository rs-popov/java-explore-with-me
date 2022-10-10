package ru.practicum.ewm.model.dto;

import ru.practicum.ewm.model.Attributes;
import ru.practicum.ewm.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndpointHitMapper {
    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .timestamp(endpointHit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .uri(endpointHit.getUri())
                .app(endpointHit.getAttributes().getApp())
                .ip(endpointHit.getAttributes().getIp())
                .build();
    }

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .uri(endpointHitDto.getUri())
                .attributes(Attributes.builder()
                        .app(endpointHitDto.getApp())
                        .ip(endpointHitDto.getIp())
                        .build())
                .build();
    }
}