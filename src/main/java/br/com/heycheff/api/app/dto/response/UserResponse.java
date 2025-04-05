package br.com.heycheff.api.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String username;
    private Integer followers;
    private List<String> followersIds;
    private Integer following;
    private Long recipesCount;
}
