package com.example.clms.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Roles {
    ROLE_ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");
    private final String role;
}
