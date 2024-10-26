package br.com.heycheff.api.util.exception;

public class AccountNotRegisteredException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Account not Registered Exception!";
    }
}
