package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.*;
import br.com.heycheff.api.model.*;
import br.com.heycheff.api.repository.*;
import br.com.heycheff.api.util.exception.ReceitaNotFoundException;
import br.com.heycheff.api.util.exception.TagNotFoundException;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;
    @Autowired
    private ReceitaStepRepository stepRepository;
    @Autowired
    private StepProdutoRepository stepProdutoRepository;
    @Autowired
    private TagReceitaRepository tagReceitaRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private ServletContext context;


    public List<ReceitaFeed> loadFeed() {
        List<Receita> receitas = receitaRepository.findByStatus(true);
        List<ReceitaFeed> receitasFeed = new ArrayList<>();

        receitas.forEach(r -> receitasFeed
                .add(new ReceitaFeed(r.getId(), resolve(r.getThumb()), r.getTitulo())));

        return receitasFeed;
    }

    public ReceitaModal loadModal(Integer id) {
        Receita receita = receitaRepository.findById(id).orElseThrow(ReceitaNotFoundException::new);
        List<ReceitaStep> receitaSteps = stepRepository.findByReceita(receita);
        List<StepDTO> steps = new ArrayList<>();

        receitaSteps.forEach(step -> {
            StepDTO dto = new StepDTO();
            dto.setStep(step.getStep());
            dto.setPath(resolve(step.getPath()));
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
    public void atualizaStatus(ReceitaStatusDTO dto, Integer id) {
        receitaRepository.findById(id).orElseThrow(ReceitaNotFoundException::new)
                .setStatus(dto.getStatus());
    }

    private String resolve(String path) {
        return context.getContextPath() + "/media?path=" + path;
    }
}
