package ru.practicum.ewm.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ForbiddenAccessException extends RuntimeException {
    private String reason = "For the requested operation the conditions are not met.";
    private HttpStatus httpStatus = HttpStatus.FORBIDDEN;

    public ForbiddenAccessException(String message) {
        super(message);
    }
}