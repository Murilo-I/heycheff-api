package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.request.UserRequest;
import br.com.heycheff.api.app.dto.response.UserResponse;
import br.com.heycheff.api.app.usecase.UserUseCase;
import br.com.heycheff.api.data.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    final UserUseCase useCase;

    public UserController(UserUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(useCase.findById(id));
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(useCase.save(request));
    }
}
