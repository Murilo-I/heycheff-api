package br.com.heycheff.api.app.advice;

import br.com.heycheff.api.app.dto.response.ErrorMessage;
import br.com.heycheff.api.app.dto.response.Status;
import br.com.heycheff.api.util.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ReceiptNotFoundException.class,
            StepNotFoundException.class,
            UserNotFoundException.class,
            TagNotFoundException.class,
            MeasureUnitNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleNotFoundException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(
                new ErrorMessage(exception.getMessage(), new Status(
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase()
                ))
        );
    }

    @ExceptionHandler({StepNotInReceiptException.class,
            MediaException.class,
            GoogleOauthException.class,
            BadCredentialsException.class,
            UserRegistrationException.class,})
    public ResponseEntity<ErrorMessage> handleBadRequest(RuntimeException exception) {
        return ResponseEntity.badRequest().body(
                new ErrorMessage(exception.getMessage(), new Status(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase()
                ))
        );
    }
}
