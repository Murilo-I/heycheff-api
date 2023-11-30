package br.com.heycheff.api.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Sequences {

    @Id
    private String id;
    private Long seq;
}
