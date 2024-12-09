package br.com.heycheff.api.app.usecase;

import br.com.heycheff.api.app.dto.TagDTO;

import java.util.List;

public interface TagUseCase {
    List<TagDTO> listAll();

    List<TagDTO> findByReceiptId(Long id);
}
