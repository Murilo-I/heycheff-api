package br.com.heycheff.api.app.dto.request;

import br.com.heycheff.api.util.constants.ValidationMessages;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class StepRequest {

    @NotNull(message = ValidationMessages.STEP_NUMBER)
    private Integer stepNumber;
    @NotBlank(message = ValidationMessages.STEP_PREPARATION_MODE)
    private String modoPreparo;
    @Min(value = 2, message = ValidationMessages.STEP_TIME_MINUTES)
    private Integer timeMinutes;
    @NotNull(message = ValidationMessages.STEP_PRODUCTS)
    private String produtos;
    @NotNull(message = ValidationMessages.STEP_VIDEO)
    private MultipartFile video;
}
