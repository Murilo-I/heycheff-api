package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.ProdutoDescDTO;
import br.com.heycheff.api.dto.UnidadeMedidaDTO;
import br.com.heycheff.api.repository.ProdutoRepository;
import br.com.heycheff.api.repository.UnidadeMedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UnidadeMedidaRepository medidaRepository;

    public List<ProdutoDescDTO> listaProdutos() {
        return produtoRepository.findAll().stream().map(ProdutoDescDTO::fromEntity).toList();
    }

    public List<UnidadeMedidaDTO> listaUnidMedidas() {
        return medidaRepository.findAll().stream().map(UnidadeMedidaDTO::fromEntity).toList();
    }
}
