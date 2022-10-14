package ru.practicum.ewm.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.config.AppConfig;
import ru.practicum.ewm.event.model.CustomDateDeserializer;
import ru.practicum.ewm.location.model.dto.LocationDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class EventUpdateDto {
    @NotNull
    private Long eventId;

    private String annotation;

    private Long category;

    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DATE_FORMAT)
    private LocalDateTime eventDate;

    private String description;

    private LocationDto location;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    private String title;
}