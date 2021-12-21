package com.tennismauel.auth.config.security;

import com.tennismauel.auth.config.security.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf().disable()
                .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);
    }
}
