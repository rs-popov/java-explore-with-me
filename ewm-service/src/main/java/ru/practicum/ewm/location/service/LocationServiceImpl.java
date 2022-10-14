package ru.practicum.ewm.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ObjectNotFoundException;
import ru.practicum.ewm.location.model.*;
import ru.practicum.ewm.location.model.dto.LocationInputDto;
import ru.practicum.ewm.location.model.dto.LocationMapper;
import ru.practicum.ewm.location.model.dto.LocationOutputDto;
import ru.practicum.ewm.location.model.dto.LocationOutputDtoWithDistance;
import ru.practicum.ewm.location.repository.LocationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public LocationOutputDto addLocation(LocationInputDto locationInputDto) {
        if (locationRepository.findLocation(locationInputDto.getLat(), locationInputDto.getLon()).isPresent()) {
            throw new BadRequestException("Location "
                    + locationInputDto.getLat() + ":" + locationInputDto.getLon() + " is already added.");
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
        return locationRepository.searchLocations(lat, lon, distance, name, description, getPageRequest(from, size)).stream()
                .collect(Collectors.toList());
    }


    private PageRequest getPageRequest(Integer from, Integer size) {
        int page = from < size ? 0 : from / size;
        return PageRequest.of(page, size);
    }
}
