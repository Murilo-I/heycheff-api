package br.com.heycheff.api.util.exception;

public class UnidadeMedidaNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Unidade de Medida Not Found!";
    }
}
