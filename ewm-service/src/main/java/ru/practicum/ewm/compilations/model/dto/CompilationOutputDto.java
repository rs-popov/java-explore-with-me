package ru.practicum.ewm.compilations.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.event.model.dto.EventOutputShortDto;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class CompilationOutputDto {
    private Long id;
    private Set<EventOutputShortDto> events;
    private Boolean pinned;
    @Size(min = 3, max = 255)
    private String title;
}