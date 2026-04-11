package com.example.demo.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final long jwtExpirationtime = 86400000;

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpirationtime))
                .signWith(key)
                .compact();
    }
    public String extractUsername(String token){

        return extractClaim(token,Claims::getSubject);
    }

        public boolean validateToken(String token,String username){
        final String extractuser=extractUsername(token);
        return (extractuser.equals(username) && !istokenExpired(token));
        }

    private boolean istokenExpired(String token) {

       return extractClaim(token,Claims::getExpiration).before(new Date());


    }

    private <T>T extractClaim(String token,Function<Claims,T> claimresolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claimresolver.apply(claims);
    }

}
