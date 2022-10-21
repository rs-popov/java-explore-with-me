package ru.practicum.ewm.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.config.OffsetLimitPageable;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ObjectNotFoundException;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.model.dto.LocationInputDto;
import ru.practicum.ewm.location.model.dto.LocationMapper;
import ru.practicum.ewm.location.model.dto.LocationOutputDto;
import ru.practicum.ewm.location.model.dto.LocationOutputDtoWithDistance;
import ru.practicum.ewm.location.repository.LocationRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public LocationOutputDto addLocation(LocationInputDto locationInputDto) {
        if(locationRepository.findLocationByPointWithDistAndName(locationInputDto.getLat(), locationInputDto.getLon(),
                        locationInputDto.getRadius(), locationInputDto.getName()).isPresent()){
            log.warn("The location name={}, lat={}, lon={} was not added because such a location already exists in location radius",
                    locationInputDto.getName(),locationInputDto.getLat(), locationInputDto.getLon());
            throw new BadRequestException("Location "
                    + locationInputDto.getLat() + " : " + locationInputDto.getLon() + " is already added.");
        }
        Location location = LocationMapper.toLocationFromInput(locationInputDto);
        return LocationMapper.toLocationOutput(locationRepository.save(location));
    }

    @Override
    public LocationOutputDto getLocationById(Long locId) {
        Location location = locationRepository.findById(locId)
                .orElseThrow(() -> new ObjectNotFoundException("Location id=" + locId + "is not found."));
        return LocationMapper.toLocationOutput(location);
    }

    @Override
    public List<LocationOutputDtoWithDistance> searchLocations(Double lat,
                                                               Double lon,
                                                               Double distance,
                                                               String name,
                                                               String description,
                                                               Integer from,
                                                               Integer size) {
        return locationRepository.searchLocations(lat, lon, distance, name, description, OffsetLimitPageable.of(from, size)).toList();
    }
}