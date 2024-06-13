package br.com.heycheff.api.app.dto.request;

import lombok.Data;

@Data
public class UserRequest {

    private String email;
    private String password;
    private String username;
}
