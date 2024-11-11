package br.com.heycheff.api.app.usecase;

import br.com.heycheff.api.app.dto.request.ReceiptRequest;
import br.com.heycheff.api.app.dto.response.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ReceiptUseCase {
    PageResponse<ReceiptFeed> loadFeed(PageRequest pageRequest);

    PageResponse<ReceiptFeed> loadUserContent(PageRequest pageRequest, String userId);

    ReceiptModal loadModal(Long id);

    ReceiptId save(ReceiptRequest request, MultipartFile thumb);

    void updateStatus(ReceiptStatus dto, Long id);

    ReceiptNextStep nextStep(Long id);
}
