package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.ProductDTO;
import br.com.heycheff.api.dto.StepDTO;
import br.com.heycheff.api.model.MeasureUnit;
import br.com.heycheff.api.model.Product;
import br.com.heycheff.api.model.ProductDescriptions;
import br.com.heycheff.api.model.Step;
import br.com.heycheff.api.repository.ProductRepository;
import br.com.heycheff.api.repository.ReceiptRepository;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import br.com.heycheff.api.util.exception.StepNotInReceiptException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static br.com.heycheff.api.service.ReceiptServiceTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StepServiceTest {

    static final String PATH = "path";
    static final String DESC = "desc";
    static final String UNID_MEDIDA = MeasureUnit.UNIDADE.getDescription();
    static final float MEDIDA = 3f;
    static final String PREPARE_MODE = "prepare mode";
    static final Integer STEP_NUMBER = 1;

    @Autowired
    StepService stepService;
    @MockBean
    ReceiptRepository receiptRepository;
    @MockBean
    ProductRepository productRepository;
    @MockBean
    FileService fileService;
    @MockBean
    SequenceGeneratorService sequenceService;

    @Test
    void saveStepSuccessfully() {
        when(receiptRepository.findBySeqId(anyLong())).thenReturn(Optional.of(receipt()));
        when(sequenceService.generateSequence(anyString())).thenReturn(ID);
        when(productRepository.findByValue(anyString()))
                .thenReturn(Optional.of(new ProductDescriptions(DESC)));
        when(productRepository.save(any())).thenReturn(new Product());
        when(fileService.salvar(any(), anyString())).thenReturn(PATH);
        when(receiptRepository.save(any())).thenReturn(receipt());

        var step = stepService.save(dto(), multipart(), ID);

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
        assertThrows(ReceiptNotFoundException.class, () -> stepService.save(dto(), multipart(), ID));
    }

    @Test
    void deleteStepSuccessfully() {
        when(receiptRepository.findBySeqId(anyLong())).thenReturn(Optional.of(receipt()));
        when(receiptRepository.save(any())).thenReturn(receipt());
        doNothing().when(fileService).delete(anyString());
        assertDoesNotThrow(() -> stepService.delete(STEP_NUMBER, ID));
    }

    @Test
    void throwReceiptNotFoundExceptionWhenDeletingStep() {
        when(receiptRepository.findBySeqId(anyLong())).thenReturn(Optional.empty());
        assertThrows(ReceiptNotFoundException.class, () -> stepService.delete(STEP_NUMBER, ID));
    }

    @Test
    void throwStepNotInReceiptExceptionWhenDeletingStep() {
        when(receiptRepository.findBySeqId(anyLong())).thenReturn(Optional.of(receipt()));

        var exception = assertThrows(StepNotInReceiptException.class,
                () -> stepService.delete(2, ID));

        assertEquals("O Step de ID: 2 não existe para a receita de ID: 1", exception.getMessage());
    }

    public static Step step() {
        var step = new Step(1L, STEP_NUMBER, SCRAMBLED_EGGS);
        step.setProducts(List.of(
                new Product("ovo", UNID_MEDIDA, MEDIDA),
                new Product("sal", MeasureUnit.GRAMA.getDescription(), .5f)
        ));
        step.setPath(PATH);
        return step;
    }
}
