package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.app.usecase.TagUseCase;
import br.com.heycheff.api.data.model.Tags;
import br.com.heycheff.api.data.repository.RecipeRepository;
import br.com.heycheff.api.util.constants.CacheNames;
import br.com.heycheff.api.util.exception.RecipeNotFoundException;
import br.com.heycheff.api.util.mapper.TypeMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TagService implements TagUseCase {

    final RecipeRepository repository;

    public TagService(RecipeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TagDTO> listAll() {
        return Arrays.stream(Tags.values()).map(tag -> new TagDTO(tag.getId(), tag.getTag()))
                .toList();
    }

    @Override
    @Cacheable(value = CacheNames.TAGS)
    public List<TagDTO> findByRecipeId(Long id) {
        return repository.findBySeqId(id).orElseThrow(RecipeNotFoundException::new)
                .getTags().stream().map(TypeMapper::fromTagId).toList();
    }
}
