package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.StepDTO;
import br.com.heycheff.api.model.*;
import br.com.heycheff.api.repository.*;
import br.com.heycheff.api.util.exception.ReceitaNotFoundException;
import br.com.heycheff.api.util.exception.StepNotFoundException;
import br.com.heycheff.api.util.exception.StepNotInReceitaException;
import br.com.heycheff.api.util.exception.UnidadeMedidaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class StepService {
    @Autowired
    private ReceitaRepository receitaRepository;
    @Autowired
    private ReceitaStepRepository stepRepository;
    @Autowired
    private UnidadeMedidaRepository medidaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private StepProdutoRepository stepProdutoRepository;

    @Transactional
    public ReceitaStep incluir(StepDTO step, MultipartFile video, Integer receitaId) {
        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(ReceitaNotFoundException::new);

        ReceitaStep savedStep = stepRepository.save(new ReceitaStep(receita,
                step.getStep(), step.getModoPreparo()));

        step.getProdutos().forEach(produto -> {
            Optional<Produto> optProd = produtoRepository.findByDescricao(produto.getDesc());
            Produto prod = optProd.orElseGet(() -> produtoRepository.save(
                    new Produto(produto.getDesc())));

            UnidadeMedida unidadeMedida =
                    medidaRepository.findByDescricao(produto.getUnidMedida())
                            .orElseThrow(UnidadeMedidaNotFoundException::new);

            stepProdutoRepository.save(
                    new StepProduto(savedStep, prod, unidadeMedida, produto.getMedida()));
        });

        savedStep.setPath(fileService.salvar(video,
                "receitaStep_" + receitaId + "_" + savedStep.getStep()));

        return savedStep;
    }

    public void deletar(Integer stepId, Integer receitaId) {
        Receita receita = receitaRepository.findById(receitaId).orElseThrow(ReceitaNotFoundException::new);
        ReceitaStep step = stepRepository.findById(stepId).orElseThrow(StepNotFoundException::new);

        if (!step.getReceita().equals(receita))
            throw new StepNotInReceitaException(
                    String.format("O Step de ID: %d não existe para a receita de ID: %d",
                            stepId, receitaId)
            );

        stepProdutoRepository.findByStep(step).forEach(stepProdutoRepository::delete);
        stepRepository.delete(step);
        fileService.delete(step.getPath());
    }
}
