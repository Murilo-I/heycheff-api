package br.com.heycheff.api.app.usecase;

import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.data.model.Step;
import org.springframework.web.multipart.MultipartFile;

public interface StepUseCase {
    StepDTO getStep(Integer stepNumber, Long receiptId);

    Step save(StepDTO step, MultipartFile video, Long receiptId);

    Step delete(Integer stepNumber, Long receiptId);

    Step update(StepDTO step, MultipartFile video, Integer stepNumber, Long receiptId);
}
