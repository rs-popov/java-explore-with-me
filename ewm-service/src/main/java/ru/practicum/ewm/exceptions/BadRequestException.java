package ru.practicum.ewm.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BadRequestException extends RuntimeException {
    private String reason = "Server cannot process the request due to malformed request syntax or invalid request.";
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public BadRequestException(String message) {
        super(message);
    }
}