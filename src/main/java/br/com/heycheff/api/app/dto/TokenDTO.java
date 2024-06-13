package br.com.heycheff.api.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    private String token;
    private String type;
    private String userId;
    private Date issuedAt;
    private Date expiration;
}
