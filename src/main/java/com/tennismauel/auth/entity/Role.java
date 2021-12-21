package com.tennismauel.auth.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    MEMBER("ROLE_MEMBER", "회원"),
    GUEST("ROLE_GUEST", "손님");

    private final String key;
    private final String value;
}
