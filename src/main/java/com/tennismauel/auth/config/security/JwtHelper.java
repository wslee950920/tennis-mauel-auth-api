package com.tennismauel.auth.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.tennismauel.auth.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtHelper {
    private final AppProperties appProperties;

    public String createJwtForClaims(String subject, Map<String, String> claims, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Instant.now().toEpochMilli());
        calendar.add(field, amount);

        JWTCreator.Builder jwtBuilder = JWT.create().withSubject(subject);

        // Add claims
        claims.forEach(jwtBuilder::withClaim);

        // Add expiredAt and etc
        return jwtBuilder
                .withNotBefore(new Date())
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC512(appProperties.getAuth().getTokenSecret()));
    }
}
