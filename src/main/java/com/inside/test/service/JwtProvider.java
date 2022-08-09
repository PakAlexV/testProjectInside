package com.inside.test.service;

import com.inside.test.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    private final SecretKey jwtAccessSecret;

    /**
     * class constructor where the secret key is set to generate the access token
     * @param jwtAccessSecret the secret key (string)
     */
    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret
    ){
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    }

    /**
     * generates a access token for the user
     * @param user registered user who needs a access token
     * @return generated access token (string)
     */
    public String generateAccessToken(
            @NonNull User user
    ){
        if (user == null){
            throw new NullPointerException("user cant be null");
        }
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getName())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("name", user.getName())
                .compact();
    }

    /**
     * access token validation
     * @param accessToken access token (string)
     * @return true if the validation was successful and false otherwise
     */
    public boolean validateAccessToken(
            @NonNull String accessToken
    ){
        try{
            final String delete = "Bearer_";
            final String token = accessToken.trim().replace(delete, "");
            Jwts.parserBuilder()
                    .setSigningKey(jwtAccessSecret)
                    .build()
                    .parseClaimsJws(token);
            return  true;
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }
}
