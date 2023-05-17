package br.com.heycheff.api.service;

import br.com.heycheff.api.model.Tag;
import br.com.heycheff.api.repository.TagReceitaRepository;
import br.com.heycheff.api.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagReceitaRepository tagReceitaRepository;

    public List<Tag> listAll() {
        return tagRepository.findAll();
    }

    public List<Tag> findByReceitaId(Integer id) {
        return tagReceitaRepository.findByReceitaId(id);
    }
}
