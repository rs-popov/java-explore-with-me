package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.event.model.CustomDateDeserializer;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class EventInputDto {
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;

    private Location location;

    @Builder.Default
    private Boolean paid = false;

    @Builder.Default
    @PositiveOrZero
    private Long participantLimit = 0L;

    @Builder.Default
    private Boolean requestModeration = true;

    @Size(min = 3, max = 120)
    private String title;
}