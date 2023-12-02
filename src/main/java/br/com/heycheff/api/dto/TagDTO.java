package br.com.heycheff.api.dto;

import br.com.heycheff.api.model.Tags;
import br.com.heycheff.api.util.exception.TagNotFoundException;
import br.com.heycheff.api.util.map.EntityMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO implements EntityMapper<Integer> {

    private Integer id;
    private String tag;

    @Override
    public Integer toEntity() {
        return Arrays.stream(Tags.values()).filter(tag -> tag.getId().equals(this.id))
                .findFirst().orElseThrow(TagNotFoundException::new).getId();
    }
}