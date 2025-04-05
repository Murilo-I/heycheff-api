package br.com.heycheff.api.app.dto.response;

import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.app.dto.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullRecipeResponse {

    private String title;
    private List<StepDTO> steps;
    private List<TagDTO> tags;
}
