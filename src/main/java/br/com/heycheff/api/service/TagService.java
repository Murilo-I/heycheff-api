package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.TagDTO;
import br.com.heycheff.api.model.Tags;
import br.com.heycheff.api.repository.ReceiptRepository;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import br.com.heycheff.api.util.mapper.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TagService {

    final ReceiptRepository repository;
    final TypeMapper mapper;

    @Autowired
    public TagService(ReceiptRepository repository, TypeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<TagDTO> listAll() {
        return Arrays.stream(Tags.values()).map(tag -> new TagDTO(tag.getId(), tag.getTag()))
                .toList();
    }

    public List<TagDTO> findByReceiptId(Long id) {
        return repository.findBySeqId(id).orElseThrow(ReceiptNotFoundException::new)
                .getTags().stream().map(mapper::fromEntity).toList();
    }
}
