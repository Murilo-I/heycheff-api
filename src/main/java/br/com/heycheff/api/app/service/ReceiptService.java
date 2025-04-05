package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.app.dto.request.ReceiptRequest;
import br.com.heycheff.api.app.dto.response.*;
import br.com.heycheff.api.app.usecase.FileUseCase;
import br.com.heycheff.api.app.usecase.ReceiptUseCase;
import br.com.heycheff.api.app.usecase.SequenceGeneratorUseCase;
import br.com.heycheff.api.data.model.Receipt;
import br.com.heycheff.api.data.model.Step;
import br.com.heycheff.api.data.repository.ReceiptRepository;
import br.com.heycheff.api.util.constants.CacheNames;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import br.com.heycheff.api.util.mapper.TypeMapper;
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
public class ReceiptService implements ReceiptUseCase {

    final ReceiptRepository receiptRepository;
    final FileUseCase fileUseCase;
    final SequenceGeneratorUseCase sequenceUseCase;

    public ReceiptService(ReceiptRepository receiptRepository, FileUseCase fileUseCase,
                          SequenceGeneratorUseCase sequenceUseCase) {
        this.receiptRepository = receiptRepository;
        this.fileUseCase = fileUseCase;
        this.sequenceUseCase = sequenceUseCase;
    }

    @Override
    @Cacheable(value = CacheNames.FEED)
    public PageResponse<ReceiptFeed> loadFeed(PageRequest pageRequest) {
        Page<Receipt> receipts = receiptRepository.findByStatus(true, pageRequest);
        var receiptFeed = mapReceipts(receipts);
        return new PageResponse<>(receiptFeed, receipts.getTotalElements());
    }

    @Override
    public PageResponse<ReceiptFeed> loadUserContent(PageRequest pageRequest, String userId) {
        Page<Receipt> receipts = receiptRepository.findByOwnerId(userId, pageRequest);
        var userReceipts = mapReceipts(receipts);
        return new PageResponse<>(userReceipts, receipts.getTotalElements());
    }

    private List<ReceiptFeed> mapReceipts(Page<Receipt> receipts) {
        return receipts.map(receipt -> fromReceiptEntity(
                        receipt, fileUseCase.resolve(receipt.getThumb())
                )
        ).getContent();
    }

    @Override
    public ReceiptModal loadModal(Long id) {
        var receipt = validateReceipt(id);
        List<StepDTO> steps = new ArrayList<>();

        receipt.getSteps().forEach(step -> steps.add(
                fromStepEntity(step, fileUseCase.resolve(step.getPath()))
        ));

        ReceiptModal modal = new ReceiptModal();
        modal.setUserId(receipt.getOwnerId());
        modal.setSteps(steps);

        return modal;
    }

    @Override
    public List<FullReceiptResponse> findAll() {
        var fullResponse = new ArrayList<FullReceiptResponse>();
        receiptRepository.findAll().forEach(receipt -> {
            var fullReceipt = FullReceiptResponse.builder()
                    .title(receipt.getTitle())
                    .tags(receipt.getTags().stream()
                            .map(TypeMapper::fromTagId)
                            .toList())
                    .steps(receipt.getSteps().stream()
                            .map(step -> fromStepEntity(step, null))
                            .toList())
                    .build();
            fullResponse.add(fullReceipt);
        });
        return fullResponse;
    }

    @Override
    @Transactional
    public ReceiptId save(ReceiptRequest request, MultipartFile thumb) {
        Receipt receipt = new Receipt(request.getTitulo(), request.getUserId());
        receipt.setSeqId(sequenceUseCase.generateSequence(Receipt.RECEIPT_SEQUENCE));
        receipt.setTags(request.getTags().stream().map(TagDTO::toEntity).toList());
        receipt.setThumb(fileUseCase.salvar(thumb, "thumbReceita" + receipt.getSeqId()));
        receiptRepository.save(receipt);
        return new ReceiptId(receipt.getSeqId());
    }

    @Override
    @Transactional
    public void updateStatus(ReceiptStatus dto, Long id) {
        var receipt = validateReceipt(id);
        receipt.setStatus(dto.getStatus());
        receiptRepository.save(receipt);
    }

    @Override
    public ReceiptNextStep nextStep(Long id) {
        var receipt = validateReceipt(id);
        var steps = receipt.getSteps();
        var nextStep = steps.stream().map(Step::getStepNumber)
                .max(Integer::compareTo).orElse(0);

        return new ReceiptNextStep(steps.stream().map(step -> fromStepEntity(
                        step, fileUseCase.resolve(step.getPath())
                ))
                .toList(), nextStep + 1);
    }

    private Receipt validateReceipt(Long receiptId) {
        return receiptRepository.findBySeqId(receiptId).orElseThrow(ReceiptNotFoundException::new);
    }
}
