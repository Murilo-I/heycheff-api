package br.com.heycheff.api.util.exception;

public class UserNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "User Not Found!";
    }
}
