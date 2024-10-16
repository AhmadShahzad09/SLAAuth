package com.slatoolapi.authservice.security;

import com.slatoolapi.authservice.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("id", user.getId());
        Date now = new Date();
        Date exp = new Date(now.getTime() + 3600000);

        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(exp).signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

            return true;
        }
        catch (Exception e) {
            log.info("Error {}", e);
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        }
        catch (Exception e) {
            log.info("Error {}", e);
            return "bad token";
        }
    }
}
