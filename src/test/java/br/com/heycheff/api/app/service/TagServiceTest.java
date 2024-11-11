package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.usecase.TagUseCase;
import br.com.heycheff.api.data.model.Tags;
import br.com.heycheff.api.data.repository.ReceiptRepository;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TagServiceTest {

    ReceiptRepository repository = mock(ReceiptRepository.class);
    TagUseCase useCase = new TagService(repository);

    @Test
    void listAllTags() {
        var tags = useCase.listAll();
        assertEquals(16, tags.size());
        assertEquals(Tags.GREGA.getTag(), tags.get(14).getTag());
    }

    @Test
    void listTagsByReceipt() {
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.of(ReceiptServiceTest.receipt()));

        var tags = useCase.findByReceiptId(ReceiptServiceTest.ID);

        assertEquals(3, tags.size());
        assertEquals(Tags.VEGANO.getTag(), tags.get(2).getTag());
    }

    @Test
    void throwReceiptNotFoundWhenListingTagsByReceipt() {
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.empty());
        assertThrows(ReceiptNotFoundException.class, () -> useCase.findByReceiptId(ReceiptServiceTest.ID));
    }
}
