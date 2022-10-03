package ru.practicum.ewm.event.location;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class LocationDto {
    @NonNull
    private Double lat;
    @NonNull
    private Double lon;
}
