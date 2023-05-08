package com.orderengine.user.model.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum AuthoritiesConstants implements GrantedAuthority {
    EMPTY,
    CURRENT_USER;
    @Override
    public String getAuthority() {
        return name();
    }
}
