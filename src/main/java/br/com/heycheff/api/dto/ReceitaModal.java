package br.com.heycheff.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReceitaModal {

    private List<TagDTO> tags;
    private List<StepDTO> steps;
}
