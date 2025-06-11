package br.com.heycheff.api.app.advice;

import br.com.heycheff.api.app.dto.response.ErrorMessage;
import br.com.heycheff.api.app.dto.response.Status;
import br.com.heycheff.api.util.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.Collections;
import java.util.HashMap;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    static final String VALIDATION_FAILED_MSG = "Um ou mais campos estão inválidos.";

    @ExceptionHandler({RecipeNotFoundException.class,
            StepNotFoundException.class,
            UserNotFoundException.class,
            TagNotFoundException.class,
            MeasureUnitNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleNotFoundException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(
                new ErrorMessage(exception.getMessage(), new Status(
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase()
                ), Collections.emptyMap())
        );
    }

    @ExceptionHandler({StepNotInRecipeException.class,
            MediaException.class,
            GoogleOauthException.class,
            BadCredentialsException.class,
            UserRegistrationException.class,})
    public ResponseEntity<ErrorMessage> handleBadRequest(RuntimeException exception) {
        return ResponseEntity.badRequest().body(
                new ErrorMessage(exception.getMessage(), new Status(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase()
                ), Collections.emptyMap())
        );
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorMessage> handleConstraintViolation(ConstraintViolationException exception) {
        var mapErrors = new HashMap<String, String>();
        exception.getConstraintViolations().forEach(constraint ->
                mapErrors.put(constraint.getPropertyPath().toString().split("\\.")[1],
                        constraint.getMessage())
        );
        return ResponseEntity.badRequest().body(
                new ErrorMessage(VALIDATION_FAILED_MSG, new Status(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase()
                ), mapErrors)
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        var mapErrors = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                mapErrors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(
                new ErrorMessage(VALIDATION_FAILED_MSG, new Status(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase()
                ), mapErrors)
        );
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ErrorMessage> handleMethodNotAllowed(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                new ErrorMessage("GET /media is not allowed",
                        new Status(
                                HttpStatus.METHOD_NOT_ALLOWED.value(),
                                exception.getMessage()
                        ), Collections.emptyMap())
        );
    }
}
