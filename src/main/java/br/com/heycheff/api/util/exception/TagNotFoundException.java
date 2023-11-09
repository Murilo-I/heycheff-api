package br.com.heycheff.api.util.exception;

public class TagNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Tag Not Found!";
    }
}
