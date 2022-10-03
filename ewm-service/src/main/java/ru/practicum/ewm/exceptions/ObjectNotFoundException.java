package ru.practicum.ewm.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ObjectNotFoundException extends RuntimeException {
    private String reason = "The required object was not found.";
    private HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public ObjectNotFoundException(String message) {
        super(message);
    }
}