package ru.practicum.ewm.requests.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.config.AppConfig;
import ru.practicum.ewm.requests.model.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestDto {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DATE_FORMAT)
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}