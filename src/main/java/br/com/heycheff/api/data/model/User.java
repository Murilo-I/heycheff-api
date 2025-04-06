package br.com.heycheff.api.data.model;

import br.com.heycheff.api.app.dto.response.WatchedRecipe;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@Document
public class User implements UserDetails {

    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String password;
    @Indexed(unique = true)
    private String username;
    private List<Role> roles;
    private List<String> followersIds;
    private List<String> followingIds;
    private LocalDateTime lastLogin;
    private Set<WatchedRecipe> watchedRecipes;
    private List<String> recommendedRecipes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
