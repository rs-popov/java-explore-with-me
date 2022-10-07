package ru.practicum.ewm.dto;

public class ViewStatsDto {
    String app;
    String uri;
    Integer hits;

    public ViewStatsDto(String app, String uri, Integer hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
