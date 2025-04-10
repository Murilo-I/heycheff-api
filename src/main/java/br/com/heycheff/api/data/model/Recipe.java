package br.com.heycheff.api.data.model;

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
public class Recipe {

    @Transient
    public static final String RECIPE_SEQUENCE = "recipe_sequence";

    @Id
    private String id;
    private Long seqId;
    private String thumb;
    private String title;
    private LocalDateTime dateTime;
    private Boolean status = false;
    private String ownerId;
    private List<Step> steps;
    private List<Integer> tags;

    public Recipe(String title, String userId) {
        this.title = title;
        this.ownerId = userId;
        this.dateTime = LocalDateTime.now();
        this.steps = Collections.emptyList();
        this.tags = Collections.emptyList();
    }
}
