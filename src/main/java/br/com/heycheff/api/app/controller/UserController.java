package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.request.FollowRequest;
import br.com.heycheff.api.app.dto.request.RecommendationRequest;
import br.com.heycheff.api.app.dto.request.UserRequest;
import br.com.heycheff.api.app.dto.response.FollowResponse;
import br.com.heycheff.api.app.dto.response.UserRecommendationResponse;
import br.com.heycheff.api.app.dto.response.UserResponse;
import br.com.heycheff.api.app.usecase.UserUseCase;
import br.com.heycheff.api.data.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    final UserUseCase useCase;

    public UserController(UserUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserRecommendationResponse>> getAll() {
        return ResponseEntity.ok(useCase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(useCase.findById(id));
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(useCase.save(request));
    }

    @PostMapping("/follow")
    public ResponseEntity<FollowResponse> follow(@RequestBody FollowRequest request) {
        return ResponseEntity.ok(useCase.follow(request));
    }

    @PatchMapping("/{id}/recommended-recipes")
    public ResponseEntity<Void> updateUserRecommendations(@PathVariable String id,
                                                          @RequestBody RecommendationRequest request) {
        useCase.updateRecommendationList(id, request.recipesIds());
        return ResponseEntity.ok().build();
    }
}
