package com.orderengine.deliver.deliverservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {

    public static final String AUTHORITIES_KEY = "auth";
    public static final String CURRENT_ROLE_KEY = "role";
    public static final String LOGIN_KEY = "login";

    protected final String secretKey;
    protected final long tokenValidityMillis;

    public TokenProvider(
        @Value("${security.authentication.jwt.secret}") String secret,
        @Value("${security.authentication.jwt.token-validity-in-seconds}") Long tokenValidityInSecond
    ) {
        Base64.Encoder encoder = Base64.getEncoder();
        Integer multiplierToMillis = 1000;
        this.secretKey = encoder.encodeToString(
            secret.getBytes(StandardCharsets.UTF_8)
        );
        this.tokenValidityMillis = multiplierToMillis * tokenValidityInSecond;
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        Map<String, Object> details = new HashMap<>();
        details.put(CURRENT_ROLE_KEY, claims.get(CURRENT_ROLE_KEY));

        User principal = new User(claims.getSubject(), "", authorities);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, token, authorities);
        authentication.setDetails(details);
        return authentication;
    }

    public String createToken(JwtTokenConfiguration tokenConfiguration) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenValidityMillis);

        return Jwts.builder()
            .setSubject(tokenConfiguration.authenticationName)
            .claim(AUTHORITIES_KEY, tokenConfiguration.authorities)
            .claim(CURRENT_ROLE_KEY, tokenConfiguration.currentRole)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .setExpiration(validity)
            .compact();
    }

    @Data
    @AllArgsConstructor
    public static class JwtTokenConfiguration {
        String authenticationName;
        String authorities;
        String currentRole;

        public static JwtTokenConfiguration fromParams(Authentication authentication,
                                                       String authorities,
                                                       Map<String, Object> details) {
            return new JwtTokenConfiguration(
                authentication.getName(),
                authorities,
                (String) details.get(CURRENT_ROLE_KEY)
            );
        }
    }
}
