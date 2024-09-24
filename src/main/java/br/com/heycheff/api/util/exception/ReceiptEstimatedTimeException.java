package br.com.heycheff.api.util.exception;

public class ReceiptEstimatedTimeException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Couldn't get total steps time!";
    }
}
