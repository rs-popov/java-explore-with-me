package ru.practicum.ewm.location.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;

@Data
@Builder
public class LocationInputDto {
    @NonNull
    @DecimalMin(value = "-90.0", message = "Invalid latitude - lower then min.")
    @DecimalMax(value = "+90.0", message = "Invalid latitude - over max.")
    private Double lat;

    @NonNull
    @DecimalMin(value = "-180.0", message = "Invalid longitude - lower then min.")
    @DecimalMax(value = "+180.0", message = "Invalid longitude - over max.")
    private Double lon;

    @NotBlank
    @Size(max = 500)
    private String description;

    @NotBlank
    @Size(max = 255)
    private String name;

    @PositiveOrZero
    private Double radius;
}