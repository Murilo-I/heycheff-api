package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.*;
import br.com.heycheff.api.model.Receipt;
import br.com.heycheff.api.model.Step;
import br.com.heycheff.api.repository.ReceiptRepository;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import br.com.heycheff.api.util.mapper.TypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static br.com.heycheff.api.util.mapper.TypeMapper.fromStepEntity;

@Service
public class ReceiptService {

    final ReceiptRepository receiptRepository;
    final FileService fileService;
    final SequenceGeneratorService sequenceService;

    public ReceiptService(ReceiptRepository receiptRepository, FileService fileService,
                          SequenceGeneratorService sequenceService) {
        this.receiptRepository = receiptRepository;
        this.fileService = fileService;
        this.sequenceService = sequenceService;
    }

    public List<ReceiptFeed> loadFeed() {
        List<Receipt> receipts = receiptRepository.findByStatus(true);
        List<ReceiptFeed> receiptFeed = new ArrayList<>();

        receipts.forEach(r -> receiptFeed
                .add(new ReceiptFeed(r.getSeqId(),
                        fileService.resolve(r.getThumb()),
                        r.getTitle())
                ));

        return receiptFeed;
    }

    public ReceiptModal loadModal(Long id) {
        var receipt = validateReceipt(id);
        List<StepDTO> steps = new ArrayList<>();

        receipt.getSteps().forEach(step -> steps.add(
                fromStepEntity(step, fileService.resolve(step.getPath()))
        ));

        List<TagDTO> tags = receipt.getTags().stream().map(TypeMapper::fromTagId).toList();

        ReceiptModal modal = new ReceiptModal();
        modal.setSteps(steps);
        modal.setTags(tags);

        return modal;
    }

    @Transactional
    public Receipt save(ReceiptRequest request, MultipartFile thumb) {
        Receipt receipt = new Receipt(request.getTitulo());
        receipt.setSeqId(sequenceService.generateSequence(Receipt.RECEIPT_SEQUENCE));
        receipt.setTags(request.getTags().stream().map(TagDTO::toEntity).toList());
        receipt.setThumb(fileService.salvar(thumb, "thumbReceita" + receipt.getSeqId()));

        return receiptRepository.save(receipt);
    }

    @Transactional
    public void updateStatus(ReceiptStatus dto, Long id) {
        var receipt = validateReceipt(id);
        receipt.setStatus(dto.getStatus());
        receiptRepository.save(receipt);
    }

    public ReceiptNextStep nextStep(Long id) {
        var receipt = validateReceipt(id);
        var steps = receipt.getSteps();
        var nextStep = steps.stream().map(Step::getStepNumber)
                .max(Integer::compareTo).orElse(0);

        return new ReceiptNextStep(steps.stream().map(step -> fromStepEntity(
                        step, fileService.resolve(step.getPath())
                ))
                .toList(), nextStep + 1);
    }

    private Receipt validateReceipt(Long receiptId) {
        return receiptRepository.findBySeqId(receiptId).orElseThrow(ReceiptNotFoundException::new);
    }
}
