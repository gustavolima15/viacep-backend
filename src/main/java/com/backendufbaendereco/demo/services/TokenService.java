package com.backendufbaendereco.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.backendufbaendereco.demo.Exeption.AuthException;
import com.backendufbaendereco.demo.entities.user.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String secret;
    public String generateToken(User user) {

        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create().withIssuer("auth")
                    .withSubject(user.getEmail())
                    .withClaim("userId", user.getId())
                    .withClaim("userName", user.getName())
                    .withClaim("role", String.valueOf(user.getRole()))
                    .withExpiresAt(ExpirationDate())
                    .sign(algorithm);

        }catch (JWTCreationException exception){
            throw new AuthException("Error while generating token");
        }

    }

    private Instant ExpirationDate() {

        return LocalDateTime.now().plusMinutes(10).toInstant(ZoneOffset.of("-03:00"));
    }
    public String validateToken(String token){

        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);
            return  JWT.require(algorithm).withIssuer("auth").build().verify(token).getSubject();

        }catch (Exception exception){

            throw new AuthException( "Invalid token");

        }
    }
}
