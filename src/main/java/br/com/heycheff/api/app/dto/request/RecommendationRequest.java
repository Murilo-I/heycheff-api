package br.com.heycheff.api.app.dto.request;

import java.util.List;

public record RecommendationRequest(List<String> recipesIds) {
}
