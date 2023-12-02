package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.*;
import br.com.heycheff.api.model.Receipt;
import br.com.heycheff.api.repository.ReceiptRepository;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import br.com.heycheff.api.util.map.TypeMapper;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceiptService {

    @Autowired
    ReceiptRepository receiptRepository;
    @Autowired
    FileService fileService;
    @Autowired
    ServletContext context;
    @Autowired
    SequenceGeneratorService sequenceService;
    @Autowired
    TypeMapper mapper;

    public List<ReceiptFeed> loadFeed() {
        List<Receipt> receipts = receiptRepository.findByStatus(true);
        List<ReceiptFeed> receiptFeed = new ArrayList<>();

        receipts.forEach(r -> receiptFeed
                .add(new ReceiptFeed(r.getSeqId(), resolve(r.getThumb()), r.getTitle())));

        return receiptFeed;
    }

    public ReceitaModal loadModal(Long id) {
        Receipt receipt = receiptRepository.findBySeqId(id).orElseThrow(ReceiptNotFoundException::new);
        List<StepDTO> steps = new ArrayList<>();

        receipt.getSteps().forEach(step -> {
            StepDTO dto = new StepDTO();
            dto.setStep(step.getStep());
            dto.setPath(resolve(step.getPath()));
            dto.setModoPreparo(step.getPreparationMode());
            dto.setProdutos(step.getProducts().stream()
                    .map(mapper::fromEntity).toList());
            steps.add(dto);
        });

        List<TagDTO> tags = receipt.getTags().stream().map(mapper::fromEntity).toList();

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

    private String resolve(String path) {
        return context.getContextPath() + "/media?path=" + path;
    }
}
