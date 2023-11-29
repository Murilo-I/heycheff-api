package br.com.heycheff.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Step {

    private String path;
    private Integer step;
    private String modoPreparo;
    List<Product> products;
}
