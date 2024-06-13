package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.ProductDTO;
import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.data.model.MeasureUnit;
import br.com.heycheff.api.data.model.Product;
import br.com.heycheff.api.data.model.ProductDescriptions;
import br.com.heycheff.api.data.model.Step;
import br.com.heycheff.api.data.repository.ProductRepository;
import br.com.heycheff.api.data.repository.ReceiptRepository;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import br.com.heycheff.api.util.exception.StepNotInReceiptException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class StepServiceTest {

    static final String PATH = "path";
    static final String DESC = "desc";
    static final String UNID_MEDIDA = MeasureUnit.UNIDADE.getDescription();
    static final float MEDIDA = 3f;
    static final String PREPARE_MODE = "prepare mode";
    static final Integer STEP_NUMBER = 1;

    ReceiptRepository receiptRepository = mock(ReceiptRepository.class);
    ProductRepository productRepository = mock(ProductRepository.class);
    FileService fileService = mock(FileService.class);
    SequenceGeneratorService sequenceService = mock(SequenceGeneratorService.class);
    StepService stepService = new StepService(
            receiptRepository, productRepository, fileService, sequenceService
    );

    @Test
    void saveStepSuccessfully() {
        when(receiptRepository.findBySeqId(anyLong())).thenReturn(Optional.of(ReceiptServiceTest.receipt()));
        when(sequenceService.generateSequence(anyString())).thenReturn(ReceiptServiceTest.ID);
        when(productRepository.findByValue(anyString()))
                .thenReturn(Optional.of(new ProductDescriptions(DESC)));
        when(productRepository.save(any())).thenReturn(new Product());
        when(fileService.salvar(any(), anyString())).thenReturn(PATH);
        when(receiptRepository.save(any())).thenReturn(ReceiptServiceTest.receipt());

        var step = stepService.save(dto(), ReceiptServiceTest.multipart(), ReceiptServiceTest.ID);

        Assertions.assertEquals(2, step.getProducts().size());
        Assertions.assertEquals(PREPARE_MODE, step.getPreparationMode());
        Assertions.assertEquals(PATH, step.getPath());
    }

    StepDTO dto() {
        return new StepDTO(PATH, STEP_NUMBER, List.of(
                new ProductDTO(DESC, UNID_MEDIDA, MEDIDA),
                new ProductDTO(DESC, UNID_MEDIDA, MEDIDA)
        ), PREPARE_MODE);
    }

    @Test
    void throwReceiptNotFoundExceptionWhenSavingStep() {
        when(receiptRepository.findBySeqId(anyLong())).thenReturn(Optional.empty());
        assertThrows(ReceiptNotFoundException.class, () -> stepService.save(dto(), ReceiptServiceTest.multipart(), ReceiptServiceTest.ID));
    }

    @Test
    void deleteStepSuccessfully() {
        when(receiptRepository.findBySeqId(anyLong())).thenReturn(Optional.of(ReceiptServiceTest.receipt()));
        when(receiptRepository.save(any())).thenReturn(ReceiptServiceTest.receipt());
        doNothing().when(fileService).delete(anyString());
        assertDoesNotThrow(() -> stepService.delete(STEP_NUMBER, ReceiptServiceTest.ID));
    }

    @Test
    void throwReceiptNotFoundExceptionWhenDeletingStep() {
        when(receiptRepository.findBySeqId(anyLong())).thenReturn(Optional.empty());
        assertThrows(ReceiptNotFoundException.class, () -> stepService.delete(STEP_NUMBER, ReceiptServiceTest.ID));
    }

    @Test
    void throwStepNotInReceiptExceptionWhenDeletingStep() {
        when(receiptRepository.findBySeqId(anyLong())).thenReturn(Optional.of(ReceiptServiceTest.receipt()));

        var exception = assertThrows(StepNotInReceiptException.class,
                () -> stepService.delete(2, ReceiptServiceTest.ID));

        assertEquals("O Step de ID: 2 não existe para a receita de ID: 1", exception.getMessage());
    }

    public static Step step() {
        var step = new Step(1L, STEP_NUMBER, ReceiptServiceTest.SCRAMBLED_EGGS);
        step.setProducts(List.of(
                new Product("ovo", UNID_MEDIDA, MEDIDA),
                new Product("sal", MeasureUnit.GRAMA.getDescription(), .5f)
        ));
        step.setPath(PATH);
        return step;
    }
}
