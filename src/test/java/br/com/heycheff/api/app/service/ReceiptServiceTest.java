package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.app.dto.request.ReceiptRequest;
import br.com.heycheff.api.app.dto.response.ReceiptStatus;
import br.com.heycheff.api.app.usecase.FileUseCase;
import br.com.heycheff.api.app.usecase.ReceiptUseCase;
import br.com.heycheff.api.app.usecase.SequenceGeneratorUseCase;
import br.com.heycheff.api.data.model.Receipt;
import br.com.heycheff.api.data.model.Step;
import br.com.heycheff.api.data.repository.ReceiptRepository;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.heycheff.api.app.service.StepServiceTest.step;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ReceiptServiceTest {

    public static final String SCRAMBLED_EGGS = "scrambled eggs";
    public static final long ID = 1L;
    static final String THUMB = "thumb";

    ReceiptRepository repository = mock(ReceiptRepository.class);
    FileUseCase fileUseCase = mock(FileUseCase.class);
    SequenceGeneratorUseCase seqGenUseCase = mock(SequenceGeneratorUseCase.class);
    ReceiptUseCase receiptUseCase = new ReceiptService(repository, fileUseCase, seqGenUseCase);

    @Test
    void loadFeedSuccessfully() {
        when(repository.findByStatus(anyBoolean(), any()))
                .thenReturn(new PageImpl<>(
                                Collections.singletonList(receipt())
                        )
                );

        var feed = receiptUseCase.loadFeed(PageRequest.of(1, 1));

        assertFalse(feed.items().isEmpty());
        assertEquals(SCRAMBLED_EGGS, feed.items().get(0).getTitulo());
    }

    public static Receipt receipt() {
        var receipt = new Receipt(SCRAMBLED_EGGS);
        var steps = new ArrayList<Step>();
        steps.add(step());
        receipt.setSteps(steps);
        receipt.setTags(List.of(1, 2, 3));
        receipt.setThumb(THUMB);
        receipt.setSeqId(ID);
        return receipt;
    }

    @Test
    void loadModalSuccessfully() {
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.of(receipt()));

        var modal = receiptUseCase.loadModal(ID);

        assertNotNull(modal);
        assertEquals(step().getPreparationMode(), modal.getSteps().get(0).getModoPreparo());
    }

    @Test
    void throwReceiptNotFoundWhenLoadingModal() {
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.empty());

        var exception = assertThrows(ReceiptNotFoundException.class,
                () -> receiptUseCase.loadModal(ID));

        assertEquals("Receita Not Found!", exception.getMessage());
    }

    @Test
    void saveReceiptSuccessfully() {
        var expected = receipt();
        when(repository.save(any())).thenReturn(expected);
        when(seqGenUseCase.generateSequence(anyString())).thenReturn(ID);
        when(fileUseCase.salvar(any(), anyString())).thenReturn(THUMB);

        var receipt = receiptUseCase.save(request(), multipart());

        assertEquals(expected.getSeqId(), receipt.getSeqId());
    }

    ReceiptRequest request() {
        return new ReceiptRequest(SCRAMBLED_EGGS,
                Collections.singletonList(new TagDTO(1, "salgado")));
    }

    public static MockMultipartFile multipart() {
        return new MockMultipartFile("multipart", new byte[]{});
    }

    @Test
    void updateReceiptStatusSuccessfully() {
        when(repository.save(any())).thenReturn(receipt());
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.of(receipt()));

        assertDoesNotThrow(() -> receiptUseCase.updateStatus(status(), ID));
        verify(repository, times(1)).save(any());
    }

    @Test
    void throwReceiptNotFoundWhenUpdatingStatus() {
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.empty());

        assertThrows(ReceiptNotFoundException.class,
                () -> receiptUseCase.updateStatus(status(), ID));
        verify(repository, times(0)).save(any());
    }

    ReceiptStatus status() {
        return new ReceiptStatus(true);
    }
}
