package ru.practicum.ewm.categories.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CategoryDto {
    @NotBlank
    private String name;
}