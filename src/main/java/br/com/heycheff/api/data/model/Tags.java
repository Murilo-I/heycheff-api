package br.com.heycheff.api.data.model;

public enum Tags {

    SALGADO(1, "Salgado"),
    DOCE(2, "Doce"),
    VEGANO(3, "Vegano"),
    VEGETARIANO(4, "Vegetariano"),
    FAST_FOOD(5, "Fast Food"),
    MASSA(6, "Massa"),
    ITALIANA(7, "Italiana"),
    BRASILEIRA(8, "Brasileira"),
    NORDESTINA(9, "Nordestina"),
    JAPONESA(10, "Japonesa"),
    MEXICANA(11, "Mexicana"),
    CHINESA(12, "Chinesa"),
    INDIANA(13, "Indiana"),
    TAILANDESA(14, "Tailandesa"),
    GREGA(15, "Grega"),
    ARABE(16, "Árabe");

    private final Integer id;
    private final String tag;

    Tags(Integer id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public Integer getId() {
        return this.id;
    }

    public String getTag() {
        return this.tag;
    }
}
