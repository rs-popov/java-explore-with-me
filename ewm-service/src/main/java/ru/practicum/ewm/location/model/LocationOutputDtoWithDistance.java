package ru.practicum.ewm.location.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
public class LocationOutputDtoWithDistance {

    private Long id;


    private Double lat;

    private Double lon;

    private String description;


    private Double radius;

    private Double distance;
}
