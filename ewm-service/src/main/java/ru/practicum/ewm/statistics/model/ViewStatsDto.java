package ru.practicum.ewm.statistics.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
public class ViewStatsDto implements Serializable {
    @NotBlank
    @Size(min = 3, max = 255)
    String app;
    @NotBlank
    @Size(min = 3, max = 255)
    String uri;
    @PositiveOrZero
    Integer hits;
}