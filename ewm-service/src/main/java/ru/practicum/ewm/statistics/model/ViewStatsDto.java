package ru.practicum.ewm.statistics.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ViewStatsDto implements Serializable {
    String app;
    String uri;
    Integer hits;
}
