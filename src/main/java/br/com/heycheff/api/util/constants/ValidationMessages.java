package br.com.heycheff.api.util.constants;

public class ValidationMessages {

    private ValidationMessages() {
    }

    public static final String RECIPE_TITLE = "O título é obrigatório.";
    public static final String USER_ID = "O usuário é obrigatório";
    public static final String RECIPE_TAGS = "Selecione ao menos uma categoria.";
    public static final String RECIPE_THUMB = "Thumb não pode ser null.";
    public static final String STEP_NUMBER = "stepNumber não pode ser null.";
    public static final String STEP_PREPARATION_MODE = "O Modo de preparo não pode estar em branco.";
    public static final String STEP_TIME_MINUTES = "O tempo mínimo de um passo são 2 minutos.";
    public static final String STEP_PRODUCTS = "Selecione ao menos 1 ingrediente.";
    public static final String STEP_VIDEO = "O vídeo é obrigatório.";
}
