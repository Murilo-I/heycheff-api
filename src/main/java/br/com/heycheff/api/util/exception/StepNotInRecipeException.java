package br.com.heycheff.api.util.exception;

public class StepNotInRecipeException extends RuntimeException {

    public StepNotInRecipeException(String message) {
        super(message);
    }
}
