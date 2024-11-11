package br.com.heycheff.api.app.usecase;

import br.com.heycheff.api.app.dto.request.UserRequest;
import br.com.heycheff.api.app.dto.response.UserResponse;
import br.com.heycheff.api.data.model.User;

import java.util.Optional;

public interface UserUseCase {

    User save(UserRequest request);

    Optional<User> findByEmail(String email);

    UserResponse findById(String userId);
}
