package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.usecase.SequenceGeneratorUseCase;
import br.com.heycheff.api.data.model.Sequences;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SequenceGeneratorService implements SequenceGeneratorUseCase {

    final MongoOperations mongoOperations;

    public SequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Long generateSequence(String seqName) {
        Sequences counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq", 1), options().returnNew(true).upsert(true),
                Sequences.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
