package br.com.heycheff.api.service;

import br.com.heycheff.api.model.Tags;
import br.com.heycheff.api.repository.ReceiptRepository;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static br.com.heycheff.api.service.ReceiptServiceTest.ID;
import static br.com.heycheff.api.service.ReceiptServiceTest.receipt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class TagServiceTest {

    @Autowired
    TagService service;
    @MockBean
    ReceiptRepository repository;

    @Test
    void listAllTags() {
        var tags = service.listAll();
        assertEquals(16, tags.size());
        assertEquals(Tags.GREGA.getTag(), tags.get(14).getTag());
    }

    @Test
    void listTagsByReceipt() {
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.of(receipt()));

        var tags = service.findByReceiptId(ID);

        assertEquals(3, tags.size());
        assertEquals(Tags.VEGANO.getTag(), tags.get(2).getTag());
    }

    @Test
    void throwReceiptNotFoundWhenListingTagsByReceipt() {
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.empty());
        assertThrows(ReceiptNotFoundException.class, () -> service.findByReceiptId(ID));
    }
}
