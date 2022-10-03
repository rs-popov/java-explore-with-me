package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.user.dto.UserShortOutputDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class EventOutputShortDto {
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
    @NonNull
    private Boolean paid;
    @NotBlank
    private String title;
    private Long views;
}