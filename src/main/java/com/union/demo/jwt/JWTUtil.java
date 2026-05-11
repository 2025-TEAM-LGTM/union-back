package com.union.demo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    @PostConstruct
    private void init(){
        //secret은 최소 32바이트(256bit) 이상이어야함
        this.secretKey= Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    /*params
    category "access" / "refresh"
    userId 사용자의 Pk
    loginId 로그인 아이디
    hasRole "ROLE_USER"
    expiredMs 만료 ms
    */
    public String createJWT(String category, Long userId, String loginId, String hasRole, long expiredMs){
        Date now= new Date();
        Date exp=new Date(now.getTime()+expiredMs);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("category",category)
                .claim("userId",userId)
                .claim("hasRole",hasRole)
                .issuedAt(now)
                .expiration(exp)
                .signWith(secretKey)
                .compact();
    }

    //개발용 테스트 계정
    public String createDevAccessJWT(String category, Long userId, String loginId, String hasRole){
        Date now= new Date();
        long devExpireTime = 1000L * 60 * 60 * 24 * 90; // 90일

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("category",category)
                .claim("userId",userId)
                .claim("hasRole",hasRole)
                .issuedAt(now)
                .expiration(new Date(now.getTime()+devExpireTime))
                .signWith(secretKey)
                .compact();
    }
    public boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public String getCategory(String token) {
        return getClaims(token).get("category", String.class);
    }

    public Long getUserId(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    public String getHasRole(String token) {
        return getClaims(token).get("hasRole", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
