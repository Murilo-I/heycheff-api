package br.com.heycheff.api.data.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getAuthority() {
        return this.roleName;
    }
}
