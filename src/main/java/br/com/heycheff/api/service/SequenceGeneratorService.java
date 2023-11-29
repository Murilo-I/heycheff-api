package br.com.heycheff.api.service;

import br.com.heycheff.api.model.ReceiptSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SequenceGeneratorService {

    @Autowired
    MongoOperations mongoOperations;

    protected Long generateSequence(String seqName) {
        ReceiptSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq", 1), options().returnNew(true).upsert(true),
                ReceiptSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
