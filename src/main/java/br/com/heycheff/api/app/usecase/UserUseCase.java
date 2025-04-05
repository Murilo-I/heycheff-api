package br.com.heycheff.api.app.usecase;

import br.com.heycheff.api.app.dto.request.FollowRequest;
import br.com.heycheff.api.app.dto.request.UserRequest;
import br.com.heycheff.api.app.dto.response.FollowResponse;
import br.com.heycheff.api.app.dto.response.UserRecommendationResponse;
import br.com.heycheff.api.app.dto.response.UserResponse;
import br.com.heycheff.api.data.model.User;

import java.util.List;
import java.util.Optional;

public interface UserUseCase {

    User save(UserRequest request);

    Optional<User> findByEmail(String email);

    UserResponse findById(String userId);

    List<UserRecommendationResponse> findAll();

    FollowResponse follow(FollowRequest request);

    void updateLastLogin(String userid);

    void updateRecommendationList(String userId, List<String> recipesIds);
}
