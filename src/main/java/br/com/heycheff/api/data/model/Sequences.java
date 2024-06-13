package br.com.heycheff.api.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
public class Sequences {

    @Id
    private String id;
    private Long seq;

    public Sequences(Long seq) {
        this.seq = seq;
    }
}
