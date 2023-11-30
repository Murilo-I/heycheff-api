package br.com.heycheff.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
public class ProductDescriptions {

    @Id
    private String id;
    private String value;

    public ProductDescriptions(String description) {
        this.value = description;
    }
}
