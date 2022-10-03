package ru.practicum.ewm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice()
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException e) {
        return getErrorResponse(e.getMessage(), e.getReason(), e.getHttpStatus());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleObjectNotFoundException(ObjectNotFoundException e) {
        return getErrorResponse(e.getMessage(), e.getReason(), e.getHttpStatus());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenAccessException(ForbiddenAccessException e) {
        return getErrorResponse(e.getMessage(), e.getReason(), e.getHttpStatus());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerErrorException(Throwable e) {
        return getErrorResponse(e.getMessage(), "Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse getErrorResponse(String message, String reason, HttpStatus status) {
        return ErrorResponse.builder()
                .message(message)
                .reason(reason)
                .status(status.getReasonPhrase().toUpperCase(Locale.ROOT))
                .build();
    }
}