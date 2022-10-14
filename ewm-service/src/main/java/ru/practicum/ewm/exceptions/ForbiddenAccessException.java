package ru.practicum.ewm.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ForbiddenAccessException extends RuntimeException {
    private final String reason = "For the requested operation the conditions are not met.";
    private final HttpStatus httpStatus = HttpStatus.FORBIDDEN;

    public ForbiddenAccessException(String message) {
        super(message);
    }
}