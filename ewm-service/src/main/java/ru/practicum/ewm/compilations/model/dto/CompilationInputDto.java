package ru.practicum.ewm.compilations.model.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class CompilationInputDto {
    private Set<Long> events;
    private Boolean pinned;
    @NotBlank
    @Size(min = 3, max = 255)
    private String title;
}