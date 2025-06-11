package br.com.heycheff.api.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    private String token;
    private String type;
    private String userId;
    private Date issuedAt;
    private Date expiration;
}
