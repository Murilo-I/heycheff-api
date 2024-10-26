package br.com.heycheff.api.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {

    private String username;
    private String password;
}
