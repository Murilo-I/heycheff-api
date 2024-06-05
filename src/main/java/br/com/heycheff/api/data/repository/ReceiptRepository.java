package br.com.heycheff.api.data.repository;

import br.com.heycheff.api.data.model.Receipt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReceiptRepository extends MongoRepository<Receipt, String> {

    List<Receipt> findByStatus(boolean status);

    Optional<Receipt> findBySeqId(Long id);
}
