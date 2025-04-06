package br.com.heycheff.api.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class WatchedRecipe implements Serializable {
    private String recipeId;
    private boolean watchedEntirely;
}
