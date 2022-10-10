package ru.practicum.ewm.location.model;

public interface LocationOutputDtoWithDistance {
    Long getId();

    Double getLat();

    Double getLon();

    String getDescription();

    String getName();

    Double getRadius();

    Double getDistance();
}
