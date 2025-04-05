package br.com.heycheff.api.app.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class WatchedRecipes implements Serializable {
    private String recipeId;
    private boolean watchedEntirely;
}
