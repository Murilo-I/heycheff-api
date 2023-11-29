package br.com.heycheff.api.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
public class Usuario {
    @Id
    private String id;
    private String username;
    private String email;
    private String senha;
    private String nickname;
    private int roles;
}
