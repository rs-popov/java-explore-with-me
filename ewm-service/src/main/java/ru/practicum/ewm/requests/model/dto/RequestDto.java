package ru.practicum.ewm.requests.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.EwmService;
import ru.practicum.ewm.requests.model.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestDto {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = EwmService.DATE_FORMAT)
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}