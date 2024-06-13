package br.com.heycheff.api.app.dto.request;

import br.com.heycheff.api.app.dto.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReceiptRequest {

    private String titulo;
    private List<TagDTO> tags;
}
