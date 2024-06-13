package br.com.heycheff.api.util.exception;

public class ReceiptNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Receita Not Found!";
    }
}
