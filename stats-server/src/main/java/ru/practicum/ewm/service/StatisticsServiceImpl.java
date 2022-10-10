package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.ViewStats;
import ru.practicum.ewm.model.dto.EndpointHitDto;
import ru.practicum.ewm.model.dto.EndpointHitMapper;
import ru.practicum.ewm.repository.StatisticsRepository;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;

    @Override
    public EndpointHitDto addHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        EndpointHit result = statisticsRepository.save(endpointHit);
        log.info("Add hit at uri={} from ip={} and app={}.",
                result.getUri(), result.getAttributes().getIp(), result.getAttributes().getApp());
        return EndpointHitMapper.toEndpointHitDto(result);
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        if (unique) {
            return statisticsRepository.getViewWithUniqueIP(start, end, (uris == null ? Collections.emptyList() : uris));
        } else {
            return statisticsRepository.getViewWithDuplicateIP(start, end, (uris == null ? Collections.emptyList() : uris));
        }
    }
}