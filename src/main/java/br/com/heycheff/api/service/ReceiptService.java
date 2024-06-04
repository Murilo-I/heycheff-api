package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.*;
import br.com.heycheff.api.model.Receipt;
import br.com.heycheff.api.repository.ReceiptRepository;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import br.com.heycheff.api.util.mapper.TypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public ReceitaModal loadModal(Long id) {
        Receipt receipt = receiptRepository.findBySeqId(id).orElseThrow(ReceiptNotFoundException::new);
        List<StepDTO> steps = new ArrayList<>();

        receipt.getSteps().forEach(step -> steps.add(
                TypeMapper.fromStepEntity(step, fileService.resolve(step.getPath()))
        ));

        List<TagDTO> tags = receipt.getTags().stream().map(TypeMapper::fromTagId).toList();

        ReceitaModal modal = new ReceitaModal();
        modal.setSteps(steps);
        modal.setTags(tags);

        return modal;
    }

    @Transactional
    public Receipt save(ReceitaRequest request, MultipartFile thumb) {
        Receipt receipt = new Receipt(request.getTitulo(), LocalDateTime.now());
        receipt.setSeqId(sequenceService.generateSequence(Receipt.RECEIPT_SEQUENCE));
        receipt.setTags(request.getTags().stream().map(TagDTO::toEntity).toList());
        receipt.setThumb(fileService.salvar(thumb, "thumbReceita" + receipt.getSeqId()));

        return receiptRepository.save(receipt);
    }

    @Transactional
    public void updateStatus(ReceitaStatusDTO dto, Long id) {
        Receipt receipt = receiptRepository.findBySeqId(id)
                .orElseThrow(ReceiptNotFoundException::new);
        receipt.setStatus(dto.getStatus());
        receiptRepository.save(receipt);
    }
}
