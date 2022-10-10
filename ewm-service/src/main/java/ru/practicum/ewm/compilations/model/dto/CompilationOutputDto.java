package ru.practicum.ewm.compilations.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.event.model.dto.EventOutputShortDto;

import java.util.Set;

@Data
@Builder
public class CompilationOutputDto {
    private Long id;
    private Set<EventOutputShortDto> events;
    private Boolean pinned;
    private String title;
}