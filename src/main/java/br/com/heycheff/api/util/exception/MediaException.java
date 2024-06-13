package br.com.heycheff.api.util.exception;

public class MediaException extends RuntimeException {

    public MediaException(Throwable cause) {
        super(cause);
    }

    public MediaException(String message, Throwable cause) {
        super(message, cause);
    }
}
