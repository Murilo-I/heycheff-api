package br.com.heycheff.api.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {

    private String description;
    private Boolean audited;
    private String measureUnit;
    private Float quantity;

    public Product(String description, String measureUnit, Float quantity) {
        this.description = description;
        this.measureUnit = measureUnit;
        this.quantity = quantity;
    }
}
