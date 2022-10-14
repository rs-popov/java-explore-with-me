package ru.practicum.ewm.location.model.dto;

import ru.practicum.ewm.location.model.Location;

public class LocationMapper {
    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    public static Location toLocationFromInput(LocationInputDto locationInputDto) {
        return Location.builder()
                .lat(locationInputDto.getLat())
                .lon(locationInputDto.getLon())
                .name(locationInputDto.getName())
                .description(locationInputDto.getDescription())
                .radius(locationInputDto.getRadius())
                .build();
    }

    public static LocationOutputDto toLocationOutput(Location location) {
        return LocationOutputDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .name(location.getName())
                .description(location.getDescription())
                .radius(location.getRadius())
                .build();
    }
}