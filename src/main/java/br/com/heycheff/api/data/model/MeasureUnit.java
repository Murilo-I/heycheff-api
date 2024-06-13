package br.com.heycheff.api.data.model;

public enum MeasureUnit {
    UNIDADE("unidade"),
    GRAMA("grama"),
    A_GOSTO("a gosto"),
    XICARA_CHA("xícara de chá"),
    COLHER_SOPA("colher de sopa"),
    COLHER_CHA("colher de chá"),
    COLHER_CAFE("colher de café"),
    MILILITRO("mililitro"),
    COPO_AMERICANO("copo americano");

    private final String description;

    MeasureUnit(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}