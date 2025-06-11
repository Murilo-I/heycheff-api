package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.request.FollowRequest;
import br.com.heycheff.api.app.dto.response.WatchedRecipe;
import br.com.heycheff.api.app.usecase.AuthenticationFacade;
import br.com.heycheff.api.app.usecase.RecipeDataUseCase;
import br.com.heycheff.api.app.usecase.UserUseCase;
import br.com.heycheff.api.data.model.User;
import br.com.heycheff.api.data.repository.UserRepository;
import br.com.heycheff.api.util.exception.UserRegistrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.heycheff.api.data.helper.DataHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    UserRepository userRepository = mock(UserRepository.class);
    RecipeDataUseCase recipeData = mock(RecipeDataUseCase.class);
    AuthenticationFacade authFacade = mock(AuthenticationFacade.class);
    UserUseCase useCase = new UserService(userRepository, recipeData, authFacade);

    @BeforeEach
    void setup() {
        when(userRepository.save(any(User.class))).thenReturn(user());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user()));
    }

    @Test
    void saveUserSuccessfully() {
        assertDoesNotThrow(() -> useCase.save(userRequest()));
    }

    @Test
    void shouldThrowExceptionWhenSaving() {
        when(userRepository.save(any())).thenThrow(RuntimeException.class);
        assertThrows(UserRegistrationException.class, () -> useCase.save(userRequest()));
    }

    @Test
    void findByEmailTest() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user()));
        var user = useCase.findByEmail("email");
        assertTrue(user.isPresent());
    }

    @Test
    void findByIdTest() {
        when(recipeData.findByUserId(anyString(), any()))
                .thenReturn(new PageImpl<>(List.of(recipe())));
        var resp = useCase.findById(USER_ID);
        assertNotNull(resp);
        assertEquals(1, resp.getRecipesCount());
    }

    @Test
    void findAllTest() {
        when(userRepository.findAll()).thenReturn(List.of(user(), user()));
        var all = useCase.findAll();
        assertFalse(all.isEmpty());
        assertEquals(2, all.size());
    }

    @Test
    void shouldFollowSuccessfully() {
        var user2 = User.builder()
                .id(USER_ID.substring(11))
                .followersIds(new ArrayList<>())
                .build();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user()));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        var req = new FollowRequest();
        req.setUserId(USER_ID);
        req.setUserToFollowId(user2.getId());
        var resp = useCase.follow(req);

        assertFalse(resp.following().isEmpty());
    }

    @Test
    void shouldUnFollowSuccessfully() {
        var user2 = User.builder()
                .id(USER_ID.substring(11))
                .followersIds(new ArrayList<>())
                .followingIds(new ArrayList<>(List.of(USER_ID)))
                .build();

        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user()));

        var req = new FollowRequest();
        req.setUserId(user2.getId());
        req.setUserToFollowId(USER_ID);
        var resp = useCase.follow(req);

        assertTrue(resp.following().isEmpty());
    }

    @Test
    void cantFollowYourselfTest() {
        var req = new FollowRequest();
        req.setUserId(USER_ID);
        req.setUserToFollowId(USER_ID);
        var resp = useCase.follow(req);

        assertTrue(resp.following().isEmpty());
    }

    @Test
    void shouldUpdateLastLoginAndRecommendationList() {
        assertAll(() -> useCase.updateLastLogin(USER_ID),
                () -> useCase.updateRecommendationList(USER_ID, List.of()));
    }

    @Test
    void shouldAppendWatchedVideos() {
        Authentication authMock = mock(Authentication.class);
        when(authFacade.getAuthentication()).thenReturn(authMock);
        when(authMock.getPrincipal()).thenReturn(user());

        assertAll(() -> useCase.appendWatchedVideo(new WatchedRecipe("rid64", true)),
                () -> useCase.appendWatchedVideo(new WatchedRecipe("rid65", true)),
                () -> useCase.appendWatchedVideo(new WatchedRecipe("rid66", false)),
                () -> useCase.appendWatchedVideo(new WatchedRecipe("rid67", false))
        );
    }
}
