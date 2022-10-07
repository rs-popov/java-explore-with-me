package ru.practicum.ewm.location.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.model.LocationInputDto;
import ru.practicum.ewm.location.service.LocationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "admin/location")
public class AdminAPILocationController {
    private final LocationService locationService;

    /**
     * Добавление новой локации
     *
     * @param locationInputDto - данные добавляемой локации
     */
    @PostMapping()
    public Location addLocation(@RequestBody @Valid LocationInputDto locationInputDto) {
        return locationService.addLocation(locationInputDto);
    }

    /**
     * Получение полной информации о локации
     *
     * @param locId - id локации
     */
    @GetMapping("/{locId}")
    public Location getLocationById(@PathVariable Long locId) {
        return locationService.getLocationById(locId);
    }


    /**
     * Возвращает информацию обо всех локациях либо о локациях, имеющих соответствующее описание
     *
     * @param description - описание локации
     * @param from        - количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size        - количество элементов в наборе
     */
    @GetMapping
    public List<Location> getLocations(@RequestParam(required = false) String description,
                                       @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                       @RequestParam(defaultValue = "10") @Positive Integer size) {
        return locationService.getLocations(description, from, size);
    }

    /**
     * Возвращает информацию обо всех локациях событий, находящихся в круге диаметром distance и центром lat,lon
     *
     * @param lat      - широта точки центра поиска
     * @param lon      - долгота точки центра поиска
     * @param distance - радиус поиска
     */
    @GetMapping("/search")
    public List<Location> searchLocations(@RequestParam Double lat,
                                          @RequestParam Double lon,
                                          @RequestParam Double distance) {
        return locationService.searchLocations(lat, lon, distance);
    }
}