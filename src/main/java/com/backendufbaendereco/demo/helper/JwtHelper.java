package com.backendufbaendereco.demo.helper;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class JwtHelper {

    @Value("${jwt.secret}")
    private String secret;

    public Map<String, Claim> getUserDataFromJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

        Algorithm algorithm = Algorithm.HMAC256(secret);
        DecodedJWT jwt = JWT.require(algorithm).withIssuer("auth").build().verify(token);
        Map<String, Claim> claims = jwt.getClaims();
        return claims;


    }
}
