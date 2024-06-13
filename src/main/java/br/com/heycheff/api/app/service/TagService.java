package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.data.model.Tags;
import br.com.heycheff.api.data.repository.ReceiptRepository;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import br.com.heycheff.api.util.mapper.TypeMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TagService {

    final ReceiptRepository repository;

    public TagService(ReceiptRepository repository) {
        this.repository = repository;
    }

    public List<TagDTO> listAll() {
        return Arrays.stream(Tags.values()).map(tag -> new TagDTO(tag.getId(), tag.getTag()))
                .toList();
    }

    public List<TagDTO> findByReceiptId(Long id) {
        return repository.findBySeqId(id).orElseThrow(ReceiptNotFoundException::new)
                .getTags().stream().map(TypeMapper::fromTagId).toList();
    }
}
