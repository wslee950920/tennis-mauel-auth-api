package com.tennismauel.auth.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tennismauel.auth.api.Google.Google;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.context.annotation.RequestScope;

@Slf4j
@Configuration
public class SocialConfig {
    @RequestScope
    @Bean
    public Google google(OAuth2AuthorizedClientService clientService){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("authentication : "+authentication);

        String accessToken = null;

        if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();

            if (clientRegistrationId.equals("google")) {
                OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(clientRegistrationId, oauthToken.getName());
                accessToken = client.getAccessToken().getTokenValue();
                log.debug("accessToken : "+accessToken);
            }
        }

        return new Google(accessToken);
    }

    @Bean
    ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
