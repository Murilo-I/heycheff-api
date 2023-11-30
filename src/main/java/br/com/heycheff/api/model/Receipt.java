package br.com.heycheff.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@Document
@NoArgsConstructor
public class Receipt {

    @Transient
    public static final String RECEIPT_SEQUENCE = "receipt_sequence";

    @Id
    private String id;
    private Long idSeq;
    private String thumb;
    private String title;
    private LocalDateTime dateTime;
    private Boolean status = false;
    private List<Step> steps = Collections.emptyList();
    private List<Integer> tags = Collections.emptyList();

    public Receipt(String title, LocalDateTime dateTime) {
        this.title = title;
        this.dateTime = dateTime;
    }
}
