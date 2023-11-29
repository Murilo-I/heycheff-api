package br.com.heycheff.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReceitaRequest {

    private String titulo;
    private List<TagDTO> tags;
}
