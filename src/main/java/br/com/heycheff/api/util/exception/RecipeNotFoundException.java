package br.com.heycheff.api.util.exception;

public class RecipeNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Receita Not Found!";
    }
}
