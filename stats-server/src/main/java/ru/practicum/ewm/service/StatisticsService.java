package ru.practicum.ewm.service;

import ru.practicum.ewm.model.ViewStats;
import ru.practicum.ewm.model.dto.EndpointHitDto;

import java.util.List;

public interface StatisticsService {
    EndpointHitDto addHit(EndpointHitDto endpointHitDto);

    List<ViewStats> getStats(String rangeStart,
                             String rangeEnd,
                             List<String> uris,
                             Boolean unique);
}