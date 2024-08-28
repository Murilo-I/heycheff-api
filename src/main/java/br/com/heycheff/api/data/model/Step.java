package br.com.heycheff.api.data.model;

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
    private Integer stepNumber;
    private String preparationMode;
    private Integer timeMinutes;
    List<Product> products;

    public Step(Long stepId, Integer step, String preparationMode, Integer timeMinutes) {
        this.stepId = stepId;
        this.stepNumber = step;
        this.preparationMode = preparationMode;
        this.timeMinutes = timeMinutes;
    }
}
