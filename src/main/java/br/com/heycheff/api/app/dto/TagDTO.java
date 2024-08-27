package br.com.heycheff.api.app.dto;

import br.com.heycheff.api.data.model.Tags;
import br.com.heycheff.api.util.exception.TagNotFoundException;
import br.com.heycheff.api.util.mapper.EntityMapper;
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
        return Arrays.stream(Tags.values()).filter(value -> value.getId().equals(this.id))
                .findFirst().orElseThrow(TagNotFoundException::new).getId();
    }
}
