package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.*;
import br.com.heycheff.api.model.*;
import br.com.heycheff.api.repository.*;
import br.com.heycheff.api.util.exception.ReceitaNotFoundException;
import br.com.heycheff.api.util.exception.TagNotFoundException;
import br.com.heycheff.api.util.exception.UnidadeMedidaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UnidadeMedidaRepository medidaRepository;
    @Autowired
    private FileService fileService;


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

    @Transactional
    public Receita incluir(ReceitaRequest request, MultipartFile thumb) {
        Receita receita = receitaRepository.save(new Receita(request.getTitulo(),
                LocalDateTime.now()));

        receita.setThumb(fileService.salvar(thumb, "thumbReceita" + receita.getId()));
        request.getTags().forEach(tag -> {
            Tag savedTag = tagRepository.findById(tag.getId()).orElseThrow(TagNotFoundException::new);
            tagReceitaRepository.save(new TagReceita(receita, savedTag));
        });

        return receita;
    }

    @Transactional
    public ReceitaStep incluir(StepDTO step, MultipartFile video, Integer receitaId) {
        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(ReceitaNotFoundException::new);

        ReceitaStep savedStep = stepRepository.save(new ReceitaStep(receita,
                step.getStep(), step.getModoPreparo()));

        savedStep.setPath(fileService.salvar(video,
                "receitaStep_" + receitaId + "_" + savedStep.getStepId()));

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

        return savedStep;
    }

    public Receita atualizaStatus(ReceitaStatusDTO status) {

    }
}
