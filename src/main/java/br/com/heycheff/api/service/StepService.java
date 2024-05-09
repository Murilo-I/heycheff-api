package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.ProductDTO;
import br.com.heycheff.api.dto.StepDTO;
import br.com.heycheff.api.model.ProductDescriptions;
import br.com.heycheff.api.model.Receipt;
import br.com.heycheff.api.model.Step;
import br.com.heycheff.api.repository.ProductRepository;
import br.com.heycheff.api.repository.ReceiptRepository;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import br.com.heycheff.api.util.exception.StepNotInReceiptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class StepService {
    private static final String STEP_NOT_IN_RECEIPT_MESSAGE =
            "O Step de ID: %d não existe para a receita de ID: %d";

    @Autowired
    ReceiptRepository receiptRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    FileService fileService;
    @Autowired
    SequenceGeneratorService sequenceService;

    @Transactional
    public Step save(StepDTO step, MultipartFile video, Long receiptId) {
        Receipt receipt = receiptRepository.findBySeqId(receiptId)
                .orElseThrow(ReceiptNotFoundException::new);

        Step savedStep = new Step(sequenceService.generateSequence(Step.STEP_SEQUENCE),
                step.getStep(), step.getModoPreparo());

        savedStep.setProducts(step.getProdutos().stream().map(ProductDTO::toEntity).toList());
        step.getProdutos().forEach(product -> {
            if (productRepository.findByValue(product.getDesc()).isEmpty())
                productRepository.save(new ProductDescriptions(product.getDesc()));
        });

        savedStep.setPath(fileService.salvar(video,
                "receitaStep_" + receiptId + "_" + savedStep.getStep()));

        receipt.getSteps().add(savedStep);
        receiptRepository.save(receipt);

        return savedStep;
    }

    @Transactional
    public void delete(Long stepId, Long receiptId) {
        Receipt receipt = receiptRepository.findBySeqId(receiptId).orElseThrow(ReceiptNotFoundException::new);
        List<Step> steps = receipt.getSteps();

        Step delStep = steps.stream().filter(step -> step.getStepId().equals(stepId))
                .findFirst().orElseThrow(() -> new StepNotInReceiptException(
                        String.format(STEP_NOT_IN_RECEIPT_MESSAGE, stepId, receiptId)
                ));

        fileService.delete(delStep.getPath());
        steps.remove(delStep);
    }
}
