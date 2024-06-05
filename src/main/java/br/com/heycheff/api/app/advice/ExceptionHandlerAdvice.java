package br.com.heycheff.api.app.advice;

import br.com.heycheff.api.app.dto.response.ErrorMessage;
import br.com.heycheff.api.util.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ReceiptNotFoundException.class,
            StepNotFoundException.class,
            StepNotInReceiptException.class,
            MediaException.class,
            TagNotFoundException.class,
            MeasureUnitNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleNotFoundException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ErrorMessage(exception.getMessage()));
    }
}
