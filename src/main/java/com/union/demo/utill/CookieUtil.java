package com.union.demo.utill;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;
import java.util.Optional;

public class CookieUtil {
    public static final String REFRESH_COOKIE_NAME="refresh";

    //refresh cookie 생성
    public static void addRefreshCookie(HttpServletResponse res, String refreshToken, int maxAgeSeconds){
        ResponseCookie cookie=ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(maxAgeSeconds)
                .secure(false)  //local http
                .sameSite("Lax") //local 개발
                .build();

        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    //refresh cookie 삭제: logout
    public static void clearRefreshCookie(HttpServletResponse res){
        Cookie cookie=new Cookie(REFRESH_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); //유효기간을 0으로 만들어서 브라우저가 즉시 삭제
        cookie.setSecure(false);
        res.addCookie(cookie);
    }

    //cookie 값 읽어오기
    public static Optional<String> getCookieValue(HttpServletRequest req, String name){
        if(req.getCookies()==null) return Optional.empty();

        return Arrays.stream(req.getCookies())
                .filter(c-> name.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
