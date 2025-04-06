package br.com.heycheff.api.util.exception;

public class RecipeEstimatedTimeException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Couldn't get total steps time!";
    }
}
