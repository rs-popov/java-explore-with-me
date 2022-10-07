package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.dto.ViewStatsDtoTest;
import ru.practicum.ewm.service.StatisticsService;

import java.util.List;


@CrossOrigin(origins = "*")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    public EndpointHitDto addHit(@RequestBody EndpointHitDto endpointHitDto) {
        System.out.println(endpointHitDto);
        return statisticsService.addHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDtoTest> getStats(@RequestParam() String start,
                                           @RequestParam() String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        System.out.println(start + "    "+ end + "    "+ uris);
        return statisticsService.getStats(start, end, uris, unique);
    }
}
