package br.com.heycheff.api.dto;

import br.com.heycheff.api.model.Tags;
import br.com.heycheff.api.util.exception.TagNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    private Integer id;
    private String tag;

    public static TagDTO fromEntity(Tags tags) {
        return new TagDTO(tags.getId(), tags.getTag());
    }

    public Tags toEntity() {
        return Arrays.stream(Tags.values()).filter(tag -> tag.getId().equals(this.id))
                .findFirst().orElseThrow(TagNotFoundException::new);
    }
}
