package ru.practicum.ewm.location.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@Builder
public class LocationInputDto {
    @NonNull
    @Size(min = -90, max = +90)
    private Double lat;

    @NonNull
    @Size(min = -180, max = 180)
    private Double lon;

    private String description;

    @PositiveOrZero
    private Double radius;
}