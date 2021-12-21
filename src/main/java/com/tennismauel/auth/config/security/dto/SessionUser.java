package com.tennismauel.auth.config.security.dto;

import com.tennismauel.auth.entity.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String email;
    private String profile;

    public SessionUser(User user) {
        this.email = user.getEmail();
        this.profile = user.getProfile();
    }
}
