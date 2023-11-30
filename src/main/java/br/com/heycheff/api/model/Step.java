package br.com.heycheff.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Data
@NoArgsConstructor
public class Step {

    @Transient
    public static final String STEP_SEQUENCE = "step_sequence";

    private Long stepId;
    private String path;
    private Integer step;
    private String preparationMode;
    List<Product> products;

    public Step(Long stepId, Integer step, String preparationMode) {
        this.stepId = stepId;
        this.step = step;
        this.preparationMode = preparationMode;
    }
}
