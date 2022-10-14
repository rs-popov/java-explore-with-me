package ru.practicum.ewm.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ObjectNotFoundException extends RuntimeException {
    private final String reason = "The required object was not found.";
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public ObjectNotFoundException(String message) {
        super(message);
    }
}