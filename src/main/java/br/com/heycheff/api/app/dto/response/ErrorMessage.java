package br.com.heycheff.api.app.dto.response;

import java.util.Map;

public record ErrorMessage(String errorMessage, Status status, Map<String, String> details) {
}
