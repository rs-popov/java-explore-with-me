package ru.practicum.ewm.location.service;

import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.model.LocationInputDto;
import ru.practicum.ewm.location.model.LocationOutputDtoWithDistance;

import java.util.List;

public interface LocationService {
    Location addLocation(LocationInputDto locationInputDto);

    Location getLocationById(Long locId);

    List<LocationOutputDtoWithDistance> searchLocations(Double lat,
                                                        Double lon,
                                                        Double distance,
                                                        String name,
                                                        String description,
                                                        Integer from,
                                                        Integer size);
}
