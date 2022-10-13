package ru.practicum.ewm.model.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class EndpointHitDto {
    Long id;
    @NotBlank
    @Size(min = 3, max = 255)
    String app;
    @NotBlank
    @Size(min = 3, max = 255)
    String uri;
    @NotNull
    @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    String ip;
    @NotBlank
    String timestamp;
}
