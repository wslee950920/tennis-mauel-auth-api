package com.tennismauel.auth.config.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Slf4j
@Getter
@Builder
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;

    private String email;
    private String nick;
    private String profile;
    private Character gender;
    private Integer age;
    private String phone;
    private String provider;

    public void setNickNull(){
        this.nick=null;
    }
}
