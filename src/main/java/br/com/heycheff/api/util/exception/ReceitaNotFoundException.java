package br.com.heycheff.api.util.exception;

public class ReceitaNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Receita Not Found!";
    }
}
