package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.event.location.LocationDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.dto.UserShortOutputDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class EventOutputDto {
    private Long id;
    @NonNull
    private UserShortOutputDto initiator;
    @NotBlank
    private String annotation;
    @NonNull
    private Category category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private String description;
    private LocationDto location;
    @NonNull
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    @NonNull
    private String title;
    private EventState state;
    private Long views;
}
