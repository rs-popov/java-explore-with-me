package ru.practicum.ewm.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.config.AppConfig;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private String reason;
    private String status;
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DATE_FORMAT)
    private LocalDateTime timestamp = LocalDateTime.now();
}