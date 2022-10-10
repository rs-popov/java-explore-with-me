package ru.practicum.ewm.compilations.model.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@Builder
public class CompilationInputDto {
    private Set<Long> events;
    private Boolean pinned;
    @NotBlank
    private String title;
}