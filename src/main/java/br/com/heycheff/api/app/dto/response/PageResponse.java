package br.com.heycheff.api.app.dto.response;

import java.util.List;

public record PageResponse<T>(List<T> items, Long count) {
}
