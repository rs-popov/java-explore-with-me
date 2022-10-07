package ru.practicum.ewm.service;

import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.dto.ViewStatsDtoTest;
import ru.practicum.ewm.model.EndpointHit;

import java.util.List;

public interface StatisticsService {
    EndpointHitDto addHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDtoTest> getStats(String rangeStart,
                                    String rangeEnd,
                                    List<String> uris,
                                    Boolean unique);
}
