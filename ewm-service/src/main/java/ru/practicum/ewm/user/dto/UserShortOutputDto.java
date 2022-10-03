package ru.practicum.ewm.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserShortOutputDto {
    private Long id;
    private String name;
}
