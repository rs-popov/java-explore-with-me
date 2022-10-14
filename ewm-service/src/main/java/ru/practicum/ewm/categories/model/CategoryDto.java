package ru.practicum.ewm.categories.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Builder
public class CategoryDto {
    private Long id;
    @Size(min = 3, max = 255)
    private String name;
}