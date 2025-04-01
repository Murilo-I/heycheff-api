package br.com.heycheff.api.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasureUnitDTO implements Serializable {

    private String descricao;
}
