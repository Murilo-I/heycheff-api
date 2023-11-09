package br.com.heycheff.api.advice;

import br.com.heycheff.api.dto.ErrorMessage;
import br.com.heycheff.api.util.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ReceitaNotFoundException.class,
            StepNotFoundException.class,
            StepNotInReceitaException.class,
            TagNotFoundException.class,
            UnidadeMedidaNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleNotFoundException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
