package com.iqbal.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.iqbal.blog.entity.Author;
import com.iqbal.blog.model.response.JwtClaim;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@Slf4j
public class JwtUtils {

    @Value("${app.blog.app-name}")
    private String appName;

    @Value("${app.blog.jwt-secret}")
    private String secretKey;

    @Value("${app.blog.jwt-expirationInSecond}")
    private long expirationInSecond;

    public String generateToken(Author user){
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            List<String> parts = user.getRoles()
                    .stream()
                    .map(part -> part.getPart().name()).toList();

            return JWT.create()
                    .withIssuer(appName)
                    .withSubject(user.getId())
                    .withExpiresAt(Instant.now().plusSeconds(expirationInSecond))
                    .withClaim("roles", parts)
                    .sign(algorithm);
        } catch (JWTCreationException e){
            log.error("error while creating jwt token: {}", e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public boolean verifyJwtToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getIssuer().equals(appName);
        }catch (JWTVerificationException e){
            log.error("invalid verification JWT: {}", e.getMessage());
            return false;
        }
    }

    public JwtClaim getUserInfoByToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

            return JwtClaim.builder()
                    .userId(decodedJWT.getSubject())
                    .roles(roles)
                    .build();
        } catch (JWTVerificationException e) {
            log.error("invalid verification JWT: {}", e.getMessage());
            return null;
        }
    }
}
