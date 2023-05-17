package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.ProdutoDescDTO;
import br.com.heycheff.api.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public List<ProdutoDescDTO> listAll() {
        return repository.findAll().stream().map(ProdutoDescDTO::fromEntity).toList();
    }
}
