package ru.practicum.ewm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class StatisticsClient extends BaseClient {

    @Autowired
    public StatisticsClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
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


    public ViewStatsDto[] getStatsForEvent(Long eventId) {
        String path = "/stats?start=2020-05-05 00:00:00&end=2030-05-05 00:00:00&uris=/events/"+eventId;
        //return super.rest.getForObject(path, ViewStatsDto[].class);
//        ResponseEntity<ViewStatsDto[]> response = super.rest.getForEntity(path, ViewStatsDto[].class, eventId);
//        if(response.getStatusCode() == HttpStatus.OK) {
//            System.out.println(Arrays.toString(response.getBody()));
//            return response.getBody();
//        } else {
//            return null;
//        }
        try {
            return rest.getForObject(path, ViewStatsDto[].class);
        } catch (Exception e) {
            System.out.println("@@@@@@@"+ e.getLocalizedMessage());
        }
        System.out.println("111111");
        return new ViewStatsDto[0];
    }
}
