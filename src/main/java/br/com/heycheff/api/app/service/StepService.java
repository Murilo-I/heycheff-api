package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.ProductDTO;
import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.data.model.ProductDescriptions;
import br.com.heycheff.api.data.model.Receipt;
import br.com.heycheff.api.data.model.Step;
import br.com.heycheff.api.data.repository.ProductRepository;
import br.com.heycheff.api.data.repository.ReceiptRepository;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import br.com.heycheff.api.util.exception.StepNotInReceiptException;
import br.com.heycheff.api.util.mapper.TypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;

@Service
@Transactional
public class StepService {
    private static final String STEP_NOT_IN_RECEIPT_MESSAGE =
            "O Step de ID: %d não existe para a receita de ID: %d";

    final ReceiptRepository receiptRepository;
    final ProductRepository productRepository;
    final FileService fileService;
    final SequenceGeneratorService sequenceService;

    public StepService(ReceiptRepository receiptRepository, ProductRepository productRepository,
                       FileService fileService, SequenceGeneratorService sequenceService) {
        this.receiptRepository = receiptRepository;
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.sequenceService = sequenceService;
    }

    public StepDTO getStep(Integer stepNumber, Long receiptId) {
        var receipt = validateReceipt(receiptId);
        var step = validateStep(stepNumber, receipt);
        return TypeMapper.fromStepEntity(step, fileService.resolve(step.getPath()));
    }

    public Step save(StepDTO step, MultipartFile video, Long receiptId) {
        var receipt = validateReceipt(receiptId);
        var savedStep = new Step(sequenceService.generateSequence(Step.STEP_SEQUENCE),
                step.getStepNumber(), step.getModoPreparo());

        setProducts(step, savedStep);
        savedStep.setPath(fileService.salvar(video,
                "receitaStep_" + receiptId + "_" + savedStep.getStepNumber()));

        receipt.getSteps().add(savedStep);
        receiptRepository.save(receipt);

        return savedStep;
    }

    public Step delete(Integer stepNumber, Long receiptId) {
        var receipt = validateReceipt(receiptId);
        var delStep = validateStep(stepNumber, receipt);

        fileService.delete(delStep.getPath());
        receipt.getSteps().remove(delStep);
        receiptRepository.save(receipt);

        return delStep;
    }

    public Step update(StepDTO step, MultipartFile video, Integer stepNumber, Long receiptId) {
        var updStep = delete(stepNumber, receiptId);
        var receipt = validateReceipt(receiptId);
        var steps = receipt.getSteps();

        setProducts(step, updStep);
        updStep.setStepNumber(step.getStepNumber());
        updStep.setPreparationMode(step.getModoPreparo());
        updStep.setPath(fileService.salvar(video,
                "receitaStep_" + receiptId + "_" + updStep.getStepNumber()));

        steps.add(updStep);
        receipt.setSteps(steps.stream().sorted(Comparator
                .comparing(Step::getStepNumber)).toList());
        receiptRepository.save(receipt);
        return updStep;
    }

    private Receipt validateReceipt(Long receiptId) {
        return receiptRepository.findBySeqId(receiptId).orElseThrow(ReceiptNotFoundException::new);
    }

    private Step validateStep(Integer stepNumber, Receipt receipt) {
        return receipt.getSteps().stream().filter(step -> step.getStepNumber().equals(stepNumber))
                .findFirst().orElseThrow(() -> new StepNotInReceiptException(
                        String.format(STEP_NOT_IN_RECEIPT_MESSAGE, stepNumber, receipt.getSeqId())
                ));
    }

    private void setProducts(StepDTO stepDto, Step stepEntity) {
        stepEntity.setProducts(stepDto.getProdutos().stream().map(ProductDTO::toEntity).toList());
        stepDto.getProdutos().forEach(product -> {
            if (productRepository.findByValue(product.getDesc()).isEmpty())
                productRepository.save(new ProductDescriptions(product.getDesc()));
        });
    }
}
