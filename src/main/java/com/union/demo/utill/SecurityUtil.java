package com.union.demo.utill;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

//로그인한 현재사용자의 userId 꺼내기
public class SecurityUtil {
    public static Long getCurrentUserId(){
        Authentication authentication=
                SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !authentication.isAuthenticated()){
            throw new IllegalArgumentException("SecurityUtil- getCurrentUserId 오류");
        }
        Long userId=(Long)authentication.getPrincipal();
        return userId;

    }

}
