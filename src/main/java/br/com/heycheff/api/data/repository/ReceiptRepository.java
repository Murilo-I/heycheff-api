package br.com.heycheff.api.data.repository;

import br.com.heycheff.api.data.model.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReceiptRepository extends MongoRepository<Receipt, String> {

    Page<Receipt> findByStatus(boolean status, Pageable pageable);

    Optional<Receipt> findBySeqId(Long id);
}
