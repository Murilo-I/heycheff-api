package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.app.dto.request.ReceiptRequest;
import br.com.heycheff.api.app.dto.response.*;
import br.com.heycheff.api.data.model.Receipt;
import br.com.heycheff.api.data.model.Step;
import br.com.heycheff.api.data.repository.ReceiptRepository;
import br.com.heycheff.api.util.constants.CacheNames;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static br.com.heycheff.api.util.mapper.TypeMapper.fromReceiptEntity;
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

    @Cacheable(value = CacheNames.FEED)
    public PageResponse<ReceiptFeed> loadFeed(Integer pageNum, Integer pageSize) {
        Page<Receipt> receipts = receiptRepository.findByStatus(
                true, PageRequest.of(pageNum, pageSize)
        );

        List<ReceiptFeed> receiptFeed = receipts.map(receipt -> fromReceiptEntity(
                        receipt, fileService.resolve(receipt.getThumb())
                )
        ).getContent();

        return new PageResponse<>(receiptFeed, receipts.getTotalElements());
    }

    public ReceiptModal loadModal(Long id) {
        var receipt = validateReceipt(id);
        List<StepDTO> steps = new ArrayList<>();

        receipt.getSteps().forEach(step -> steps.add(
                fromStepEntity(step, fileService.resolve(step.getPath()))
        ));

        ReceiptModal modal = new ReceiptModal();
        modal.setSteps(steps);

        return modal;
    }

    @Transactional
    public ReceiptId save(ReceiptRequest request, MultipartFile thumb) {
        Receipt receipt = new Receipt(request.getTitulo());
        receipt.setSeqId(sequenceService.generateSequence(Receipt.RECEIPT_SEQUENCE));
        receipt.setTags(request.getTags().stream().map(TagDTO::toEntity).toList());
        receipt.setThumb(fileService.salvar(thumb, "thumbReceita" + receipt.getSeqId()));
        receiptRepository.save(receipt);
        return new ReceiptId(receipt.getSeqId());
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
