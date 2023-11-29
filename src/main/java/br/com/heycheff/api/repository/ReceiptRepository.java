package br.com.heycheff.api.repository;

import br.com.heycheff.api.model.Receipt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReceiptRepository extends MongoRepository<Receipt, String> {

    List<Receipt> findByStatus(boolean status);

    Optional<Receipt> findByIdSeq(Long id);
}
