package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.request.FollowRequest;
import br.com.heycheff.api.app.dto.request.UserRequest;
import br.com.heycheff.api.app.dto.response.FollowResponse;
import br.com.heycheff.api.app.dto.response.UserRecommendationResponse;
import br.com.heycheff.api.app.dto.response.UserResponse;
import br.com.heycheff.api.app.dto.response.WatchedRecipe;
import br.com.heycheff.api.app.usecase.AuthenticationFacade;
import br.com.heycheff.api.app.usecase.UserUseCase;
import br.com.heycheff.api.data.model.Role;
import br.com.heycheff.api.data.model.User;
import br.com.heycheff.api.data.repository.RecipeRepository;
import br.com.heycheff.api.data.repository.UserRepository;
import br.com.heycheff.api.util.exception.UserNotFoundException;
import br.com.heycheff.api.util.exception.UserRegistrationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.heycheff.api.util.mapper.TypeMapper.fromUser;

@Service
public class UserService implements UserUseCase {

    final UserRepository userRepository;
    final RecipeRepository recipeRepository;
    final AuthenticationFacade authFacade;

    public UserService(UserRepository userRepository, RecipeRepository recipeRepository,
                       AuthenticationFacade authFacade) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.authFacade = authFacade;
    }

    @Override
    @Transactional
    public User save(UserRequest request) {
        try {
            return userRepository.save(
                    User.builder().email(request.getEmail())
                            .username(request.getUsername())
                            .password(new BCryptPasswordEncoder()
                                    .encode(request.getPassword()))
                            .roles(Collections.singletonList(Role.USER))
                            .followersIds(Collections.emptyList())
                            .followingIds(Collections.emptyList())
                            .lastLogin(LocalDateTime.now())
                            .watchedRecipes(Collections.emptySet())
                            .recommendedRecipes(Collections.emptyList())
                            .build()
            );
        } catch (Exception e) {
            throw new UserRegistrationException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserResponse findById(String userId) {
        var recipeCount = recipeRepository.findByOwnerId(
                userId, PageRequest.of(1, 1)
        ).getTotalElements();
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return fromUser(user, recipeCount);
    }

    @Override
    public List<UserRecommendationResponse> findAll() {
        var response = new ArrayList<UserRecommendationResponse>();
        userRepository.findAll().forEach(user -> {
            var userRecommendation = UserRecommendationResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .lastLogin(user.getLastLogin())
                    .watchedRecipes(user.getWatchedRecipes())
                    .recommendedRecipes(user.getRecommendedRecipes())
                    .build();
            response.add(userRecommendation);
        });
        return response;
    }

    @Override
    @Transactional
    public FollowResponse follow(FollowRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(UserNotFoundException::new);
        var userToFollow = userRepository.findById(request.getUserToFollowId())
                .orElseThrow(() -> new UserNotFoundException("Following ID Not Found!"));
        var userFollowing = user.getFollowingIds();

        if (user.equals(userToFollow))
            return new FollowResponse(userFollowing);

        boolean followingNotRemoved = !userFollowing.removeIf(request.getUserToFollowId()::equals);
        if (followingNotRemoved)
            userFollowing.add(request.getUserToFollowId());

        user.setFollowingIds(userFollowing);
        userRepository.save(user);

        var userFollowers = userToFollow.getFollowersIds();
        boolean followerNotRemoved = !userFollowers.removeIf(request.getUserId()::equals);
        if (followerNotRemoved)
            userFollowers.add(request.getUserId());

        userToFollow.setFollowersIds(userFollowers);
        userRepository.save(userToFollow);

        return new FollowResponse(userFollowing);
    }

    @Override
    @Transactional
    public void updateLastLogin(String userid) {
        var user = userRepository.findById(userid).orElseThrow(UserNotFoundException::new);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void updateRecommendationList(String userId, List<String> recipesIds) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setRecommendedRecipes(recipesIds);
        userRepository.save(user);
    }

    @Override
    public void appendWatchedVideo(WatchedRecipe watchedRecipe) {
        var principal = (User) authFacade.getAuthentication().getPrincipal();
        var user = userRepository.findById(principal.getId()).orElseThrow(UserNotFoundException::new);
        var recipes = user.getWatchedRecipes();

        if (watchedRecipe.isWatchedEntirely())
            recipes.stream().filter(w -> w.getRecipeId().equals(watchedRecipe.getRecipeId())
                            && Boolean.FALSE.equals(w.isWatchedEntirely()))
                    .findFirst().ifPresent(recipes::remove);
        else {
            var isWatched = recipes.stream().filter(w -> w.getRecipeId()
                            .equals(watchedRecipe.getRecipeId()) && w.isWatchedEntirely())
                    .findFirst();

            if (isWatched.isPresent()) return;
        }

        recipes.add(watchedRecipe);

        if (!principal.getWatchedRecipes().equals(recipes)) {
            user.setWatchedRecipes(recipes);
            userRepository.save(user);
        }
    }
}
