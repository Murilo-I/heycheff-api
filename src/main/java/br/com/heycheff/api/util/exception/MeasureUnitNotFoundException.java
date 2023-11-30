package br.com.heycheff.api.util.exception;

public class MeasureUnitNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Unidade de Medida Not Found!";
    }
}
