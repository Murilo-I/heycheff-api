package br.com.heycheff.api.app.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class StepRequest {

    private Integer stepNumber;
    private String modoPreparo;
    private String produtos;
    private MultipartFile video;
}
