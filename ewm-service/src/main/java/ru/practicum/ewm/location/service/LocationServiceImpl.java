package ru.practicum.ewm.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ObjectNotFoundException;
import ru.practicum.ewm.location.model.LocationOutputDtoWithDistance;
import ru.practicum.ewm.location.repository.LocationRepository;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.model.LocationInputDto;
import ru.practicum.ewm.location.model.LocationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public Location addLocation(LocationInputDto locationInputDto) {
        if (locationRepository.findLocation(locationInputDto.getLat(), locationInputDto.getLon()).isPresent()) {
            throw new BadRequestException("");
        }
        Location location = LocationMapper.toLocationFromInput(locationInputDto);
        return locationRepository.save(location);
    }

    @Override
    public Location getLocationById(Long locId) {
        return locationRepository.findById(locId)
                .orElseThrow(() -> new ObjectNotFoundException("Location id=" + locId + "is not found."));
    }

    @Override
    public List<Location> getLocations(String description, Integer from, Integer size) {
        return locationRepository.findLocations(description, getPageRequest(from, size)).stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Location> searchLocations(Double lat, Double lon, Double distance) {
        return locationRepository.searchLocations(lat, lon, distance);
    }

    private PageRequest getPageRequest(Integer from, Integer size) {
        int page = from < size ? 0 : from / size;
        return PageRequest.of(page, size);
    }
}
