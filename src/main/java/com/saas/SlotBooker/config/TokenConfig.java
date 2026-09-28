package com.saas.SlotBooker.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.saas.SlotBooker.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TokenConfig {

    @Value("${api.jwt.secret}")
    private String secretKey;

    @Value("${api.jwt.expirationTimeInSeconds}")
    private Integer expirationTimeInSeconds;

    public String generateToken(User user){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withClaim("userId", user.getId().toString())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(expirationTimeInSeconds)) // Token expires in 1 hour
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }
}
