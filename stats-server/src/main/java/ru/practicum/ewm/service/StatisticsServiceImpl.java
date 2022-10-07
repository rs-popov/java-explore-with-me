package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.EndpointHitMapper;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.dto.ViewStatsDtoTest;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.repository.StatisticsRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;

    @Override
    public EndpointHitDto addHit(EndpointHitDto endpointHitDto) {
        System.out.println(endpointHitDto.getApp());
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        EndpointHit result = statisticsRepository.save(endpointHit);
        return EndpointHitMapper.toEndpointHitDto(result);
    }

    @Override
    public List<ViewStatsDtoTest> getStats(String start, String end, List<String> uris, Boolean unique) {
        if (unique){
            return statisticsRepository.getView3(start, end, (uris==null? Collections.emptyList(): uris));
        } else {

            return statisticsRepository.getView2(start, end, (uris==null? Collections.emptyList(): uris));
        }

    }
}
