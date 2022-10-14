package ru.practicum.ewm.location.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.location.model.dto.LocationInputDto;
import ru.practicum.ewm.location.model.dto.LocationOutputDto;
import ru.practicum.ewm.location.model.dto.LocationOutputDtoWithDistance;
import ru.practicum.ewm.location.service.LocationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/location")
public class AdminAPILocationController {
    private final LocationService locationService;

    /**
     * Добавление новой локации
     *
     * @param locationInputDto - данные добавляемой локации
     */
    @PostMapping()
    public LocationOutputDto addLocation(@RequestBody @Valid LocationInputDto locationInputDto) {
        return locationService.addLocation(locationInputDto);
    }

    /**
     * Получение полной информации о локации
     *
     * @param locId - id локации
     */
    @GetMapping("/{locId}")
    public LocationOutputDto getLocationById(@PathVariable Long locId) {
        return locationService.getLocationById(locId);
    }

    /**
     * Возвращает информацию обо всех локациях событий включая расстояние до места,
     * находящихся в области диаметром distance и центром lat,lon,
     * имеющих соответствующее название и описание
     *
     * @param lat         - широта точки центра области поиска
     * @param lon         - долгота точки центра области поиска
     * @param distance    - радиус области поиска
     * @param name        - наименование локации
     * @param description - описание локации
     * @param from        - количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size        - количество элементов в наборе
     */
    @GetMapping
    public List<LocationOutputDtoWithDistance> searchLocations(@RequestParam Double lat,
                                                               @RequestParam Double lon,
                                                               @RequestParam Double distance,
                                                               @RequestParam(required = false) String name,
                                                               @RequestParam(required = false) String description,
                                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                               @RequestParam(defaultValue = "10") @Positive Integer size) {
        return locationService.searchLocations(lat, lon, distance, name, description, from, size);
    }
}