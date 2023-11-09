package br.com.heycheff.api.util.exception;

public class StepNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Step Not Found!";
    }
}
