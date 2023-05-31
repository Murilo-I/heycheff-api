package br.com.heycheff.api.dto;

import br.com.heycheff.api.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReceitaRequest {

    private String titulo;
    private List<Tag> tags;
}
