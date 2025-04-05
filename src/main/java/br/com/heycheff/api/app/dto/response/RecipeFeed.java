package br.com.heycheff.api.app.dto.response;

import br.com.heycheff.api.app.dto.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeFeed {
    private Long id;
    private String thumb;
    private String titulo;
    private List<TagDTO> tags;
    private Integer estimatedTime;
}
