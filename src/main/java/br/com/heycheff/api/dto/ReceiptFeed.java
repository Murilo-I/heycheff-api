package br.com.heycheff.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceiptFeed {
    private Long id;
    private String thumb;
    private String titulo;
}
