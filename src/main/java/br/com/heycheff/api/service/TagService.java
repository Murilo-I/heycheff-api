package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.TagDTO;
import br.com.heycheff.api.model.Tags;
import br.com.heycheff.api.repository.ReceiptRepository;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import br.com.heycheff.api.util.map.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TagService {

    @Autowired
    private ReceiptRepository repository;
    @Autowired
    private TypeMapper mapper;

    public List<TagDTO> listAll() {
        return Arrays.stream(Tags.values()).map(tag -> new TagDTO(tag.getId(), tag.getTag()))
                .toList();
    }

    public List<TagDTO> findByReceiptId(Long id) {
        return repository.findByIdSeq(id).orElseThrow(ReceiptNotFoundException::new)
                .getTags().stream().map(mapper::fromEntity).toList();
    }
}
