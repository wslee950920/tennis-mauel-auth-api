package com.tennismauel.auth.config.security.handler;

import com.tennismauel.auth.config.security.exception.BadRequestException;
import com.tennismauel.auth.config.security.exception.EmailAlreadyExistException;
import com.tennismauel.auth.config.security.service.HttpCookieOAuth2AuthorizationRequestRepository;
import com.tennismauel.auth.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.tennismauel.auth.config.security.service.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(("/"));

        int code= HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        if(exception instanceof BadRequestException){
            code=HttpServletResponse.SC_BAD_REQUEST;
        } else if(exception instanceof EmailAlreadyExistException){
            code=HttpServletResponse.SC_CONFLICT;
        }

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("code", code)
                .queryParam("message", exception.getLocalizedMessage())
                .build().toUriString();

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
