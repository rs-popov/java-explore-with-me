package ru.practicum.ewm.model.dto;

import ru.practicum.ewm.EwmStatsServer;
import ru.practicum.ewm.model.Attributes;
import ru.practicum.ewm.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndpointHitMapper {
    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .timestamp(endpointHit.getTimestamp().format(DateTimeFormatter.ofPattern(EwmStatsServer.DATE_FORMAT)))
                .uri(endpointHit.getUri())
                .app(endpointHit.getAttributes().getApp())
                .ip(endpointHit.getAttributes().getIp())
                .build();
    }

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern(EwmStatsServer.DATE_FORMAT)))
                .uri(endpointHitDto.getUri())
                .attributes(Attributes.builder()
                        .app(endpointHitDto.getApp())
                        .ip(endpointHitDto.getIp())
                        .build())
                .build();
    }
}