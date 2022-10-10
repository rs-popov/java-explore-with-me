package ru.practicum.ewm.statistics.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.statistics.model.EndpointHitDto;
import ru.practicum.ewm.statistics.model.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class StatisticsClient extends BaseClient {
    private final static String RANGE_START = "2020-05-05 00:00:00";
    private final static String RANGE_END = "2030-05-05 00:00:00";

    @Autowired
    public StatisticsClient(@Value("${STATS_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addEndpointHit(String userIp, String uri) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("ExploreWithMe")
                .uri(uri)
                .ip(userIp)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        return post("/hit", endpointHitDto);
    }

    public Integer getStatsForEvent(Long eventId) {
        String path = "/stats?start=" + RANGE_START + "&end=" + RANGE_END + "&uris=/events/" + eventId;
        Integer result = 0;
        try {
            ViewStatsDto[] viewStats = rest.getForObject(path, ViewStatsDto[].class);
            if (viewStats != null && viewStats.length > 0) {
                result = viewStats[0].getHits();
            }
        } catch (Exception e) {
            log.warn("Error: {}", e.getMessage());
        }
        return result;
    }
}