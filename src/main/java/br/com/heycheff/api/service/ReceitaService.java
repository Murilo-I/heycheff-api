package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.ProdutoDTO;
import br.com.heycheff.api.dto.ReceitaFeed;
import br.com.heycheff.api.dto.ReceitaModal;
import br.com.heycheff.api.dto.StepDTO;
import br.com.heycheff.api.model.*;
import br.com.heycheff.api.repository.*;
import br.com.heycheff.api.util.exception.ReceitaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;
    @Autowired
    private ReceitaStepRepository stepRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private StepProdutoRepository stepProdutoRepository;
    @Autowired
    private TagReceitaRepository tagReceitaRepository;


    public List<ReceitaFeed> loadFeed() {
        List<Receita> receitas = receitaRepository.findAll();
        List<ReceitaFeed> receitasFeed = new ArrayList<>();

        receitas.forEach(r -> receitasFeed
                .add(new ReceitaFeed(r.getId(), r.getThumb(), r.getTitulo())));

        return receitasFeed;
    }

    public ReceitaModal loadModal(Integer id) {
        Receita receita = receitaRepository.findById(id).orElseThrow(ReceitaNotFoundException::new);
        List<ReceitaStep> receitaSteps = stepRepository.findByReceita(receita);
        List<StepDTO> steps = new ArrayList<>();

        receitaSteps.forEach(step -> {
            StepDTO dto = new StepDTO();
            dto.setStep(step.getStep());
            dto.setPath(step.getPath());
            dto.setModoPreparo(step.getModoPreparo());

            List<StepProduto> stepProdutos = stepProdutoRepository.findByStep(step);
            List<ProdutoDTO> produtos = stepProdutos.stream().map(ProdutoDTO::fromEntity).toList();

            dto.setProdutos(produtos);
            steps.add(dto);
        });

        List<Tag> tags = tagReceitaRepository.findByReceitaId(receita.getId());

        ReceitaModal modal = new ReceitaModal();
        modal.setThumb(receita.getThumb());
        modal.setTitulo(receita.getTitulo());
        modal.setSteps(steps);
        modal.setTags(tags);

        return modal;
    }

    public Receita incluir(ReceitaModal modal) {
        Receita receita = new Receita();
        receita.setDateTime(LocalDateTime.now());
        receita.setThumb(modal.getThumb());
        receita.setTitulo(modal.getTitulo());
        receitaRepository.save(receita);

        modal.getTags().forEach(tag -> tagReceitaRepository.save(new TagReceita(receita, tag)));

        List<StepDTO> steps = modal.getSteps();
        steps.forEach(step -> {
            ReceitaStep savedStep = stepRepository.save(new ReceitaStep(receita, step.getPath(),
                    step.getStep(), steps.size(), step.getModoPreparo()));

            step.getProdutos().forEach(produto -> {
                Optional<Produto> optProd = produtoRepository.findByDescricao(produto.getDesc());
                Produto prod = optProd.orElseGet(() -> produtoRepository.save(new Produto(produto.getDesc())));
                stepProdutoRepository.save(new StepProduto(savedStep, prod,
                        new UnidadeMedida(produto.getUnidMedida()), produto.getMedida()));
            });
        });

        return receita;
    }

}
