package br.com.heycheff.api.data.helper;

import br.com.heycheff.api.app.dto.ProductDTO;
import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.app.dto.request.ClerkRequest;
import br.com.heycheff.api.app.dto.request.RecipeRequest;
import br.com.heycheff.api.app.dto.request.UserRequest;
import br.com.heycheff.api.app.dto.response.WatchedRecipe;
import br.com.heycheff.api.data.model.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

public class DataHelper {

    public static final long ID = 1L;
    public static final String PATH = "path";
    public static final String THUMB = "thumb";
    public static final String DESC = "banana";
    public static final String USER_ID = "6744ef2d210d581f27826e05";
    public static final String SCRAMBLED_EGGS = "scrambled eggs";
    public static final String PREPARE_MODE = "prepare mode";
    public static final Integer STEP_NUMBER = 1;

    static final float MEDIDA = 3f;
    static final String UNID_MEDIDA = MeasureUnit.UNIDADE.getDescription();

    public static Recipe recipe() {
        var recipe = new Recipe(SCRAMBLED_EGGS, USER_ID);
        var steps = new ArrayList<Step>();
        steps.add(step());

        var step2 = step();
        step2.setStepNumber(2);
        step2.setStepId(2L);
        steps.add(step2);

        recipe.setSteps(steps);
        recipe.setTags(List.of(1, 2, 3));
        recipe.setThumb(THUMB);
        recipe.setSeqId(ID);
        return recipe;
    }

    public static RecipeRequest request() {
        return new RecipeRequest(SCRAMBLED_EGGS, USER_ID,
                Collections.singletonList(new TagDTO(1, "salgado")));
    }

    public static Step step() {
        var step = new Step(1L, STEP_NUMBER, SCRAMBLED_EGGS, 15);
        step.setProducts(List.of(
                new Product("ovo", UNID_MEDIDA, MEDIDA),
                new Product("sal", MeasureUnit.GRAMA.getDescription(), .5f)
        ));
        step.setPath(PATH);
        return step;
    }

    public static StepDTO dto() {
        return new StepDTO(PATH, STEP_NUMBER, List.of(
                new ProductDTO(DESC, UNID_MEDIDA, MEDIDA),
                new ProductDTO(DESC, UNID_MEDIDA, MEDIDA)
        ), PREPARE_MODE, 15);
    }

    public static User user() {
        return User.builder()
                .id(USER_ID)
                .username("ton")
                .email("emailDoTon")
                .followingIds(new ArrayList<>())
                .followersIds(new ArrayList<>())
                .watchedRecipes(new HashSet<>(Set.of(
                        new WatchedRecipe("rid64", true),
                        new WatchedRecipe("rid65", false),
                        new WatchedRecipe("rid66", true)
                )))
                .build();
    }

    public static UserRequest userRequest() {
        var request = new UserRequest();
        request.setEmail("emailDoTon");
        request.setUsername("ton");
        request.setPassword("password1234");
        return request;
    }

    public static PageRequest pageRequest() {
        return PageRequest.of(1, 1);
    }

    public static MockMultipartFile multipart(String name) {
        return new MockMultipartFile(
                name, "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    }

    public static ClerkRequest clerkRequest() {
        return new ClerkRequest("sessionId", "uEmail");
    }
}
