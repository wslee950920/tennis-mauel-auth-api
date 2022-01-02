package com.tennismauel.auth.config.security.handler;

import com.tennismauel.auth.config.AppProperties;
import com.tennismauel.auth.config.security.JwtHelper;
import com.tennismauel.auth.config.security.SecurityConfig;
import com.tennismauel.auth.config.security.exception.BadRequestException;
import com.tennismauel.auth.config.security.service.HttpCookieOAuth2AuthorizationRequestRepository;
import com.tennismauel.auth.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tennismauel.auth.config.security.service.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AppProperties appProperties;
    private final JwtHelper jwtHelper;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response);
        log.debug("targetUrl : "+targetUrl);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);

            return;
        }

        log.debug(authentication.toString());
        DefaultOAuth2User oAuth2User=(DefaultOAuth2User) authentication.getPrincipal();
        log.debug(oAuth2User.toString());
        String username=oAuth2User.getAttribute("email");
        log.debug(username);

        Map<String, String> claims = new HashMap<>();
        claims.put("username", username);

        String authorities=oAuth2User.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        claims.put(SecurityConfig.AUTHORITIES_CLAIM_NAME, authorities);

        String jwt = jwtHelper.createJwtForClaims("tennis_mauel", claims, Calendar.MINUTE, 30);
        CookieUtils.addCookie(response, "access_token", jwt, 60*30);

        clearAuthenticationAttributes(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        log.debug("redirectUri : "+redirectUri.get());

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            log.debug("bad request");

            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("code", HttpServletResponse.SC_OK)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        log.debug(appProperties.getOauth2().getAuthorizedRedirectUris().toString());
        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    log.debug("authorizedRedirectUri : "+authorizedRedirectUri);
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);

                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }

                    return false;
                });
    }
}
