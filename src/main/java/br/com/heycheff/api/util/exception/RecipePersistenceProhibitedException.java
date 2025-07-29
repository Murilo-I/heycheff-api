package br.com.heycheff.api.util.exception;

public class RecipePersistenceProhibitedException extends RuntimeException {

    @Override
    public String getMessage() {
        return "You cannot perform this action for you're not the author of this recipe";
    }
}
