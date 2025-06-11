package br.com.heycheff.api.app.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    private String email;
    private String password;
    private String username;
}
