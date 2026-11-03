package io.github.vadman1.eventmanager.web;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponseMessage> handleValidationException(
            Exception e
    ) {
        log.error("Got validation exception", e);

        String detailedMessage = e instanceof MethodArgumentNotValidException
                ? constructMethodArgumentNotValidException((MethodArgumentNotValidException) e)
                : e.getMessage();

        ErrorResponseMessage error = new ErrorResponseMessage(
                "Ошибка валидации запроса",
                detailedMessage,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    private String constructMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        return e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseMessage> handleGenericException(
            Exception e
    ) {
        log.error("Got server error", e);

        ErrorResponseMessage error = new ErrorResponseMessage(
                "Ошибка на сервере",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseMessage> handleNotFoundException(
            EntityNotFoundException e
    ) {
        log.error("Got error", e);

        ErrorResponseMessage error = new ErrorResponseMessage(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
