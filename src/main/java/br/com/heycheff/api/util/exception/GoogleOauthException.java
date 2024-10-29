package br.com.heycheff.api.util.exception;

public class GoogleOauthException extends RuntimeException {

    public GoogleOauthException() {
        super("Invalid Google Oauth Token!");
    }

    public GoogleOauthException(String message) {
        super(message);
    }
}
