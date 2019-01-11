package com.spa.wonoh.security.jwt;

import com.spa.wonoh.security.MemberPrinciple;
import io.jsonwebtoken.*;
import lombok.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtProvider {

    private static final Logger logger = LogManager.getLogger(JwtAuthTokenFilter.class);

    private final String jwtSecret = "mySecretKey";

    private int jwtExpiration = 86400;

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        }

        return false;
    }
    public  String generateJwtToken(Authentication authentication){
        MemberPrinciple memberPrinciple = (MemberPrinciple)authentication.getPrincipal();
        return Jwts.builder()
                            .setSubject((memberPrinciple.getEmail()))
                            .setIssuedAt(new Date())
                            .setExpiration(new Date((new Date().getTime()+jwtExpiration)))
                            .signWith(SignatureAlgorithm.HS512,jwtSecret)
                            .compact();
    }
    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                            .setSigningKey(jwtSecret)
                            .parseClaimsJws(token)
                            .getBody().getSubject();
    }
}
