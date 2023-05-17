package br.com.heycheff.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceitaFeed {
    private Integer id;
    private String thumb;
    private String titulo;
}
