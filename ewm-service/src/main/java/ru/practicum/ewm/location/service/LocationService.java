package ru.practicum.ewm.location.service;

import ru.practicum.ewm.location.model.dto.LocationInputDto;
import ru.practicum.ewm.location.model.dto.LocationOutputDto;
import ru.practicum.ewm.location.model.dto.LocationOutputDtoWithDistance;

import java.util.List;

public interface LocationService {
    LocationOutputDto addLocation(LocationInputDto locationInputDto);

    LocationOutputDto getLocationById(Long locId);

    List<LocationOutputDtoWithDistance> searchLocations(Double lat,
                                                        Double lon,
                                                        Double distance,
                                                        String name,
                                                        String description,
                                                        Integer from,
                                                        Integer size);
}
