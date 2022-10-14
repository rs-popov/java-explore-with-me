package ru.practicum.ewm.user.model.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserOutputDto {
    private Long id;
    @Size(min = 1, max = 255)
    private String name;
    @Email
    private String email;
}
