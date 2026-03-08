package com.union.demo.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil){
        this.jwtUtil=jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {

        String authorization= req.getHeader("Authorization");
        String path=req.getRequestURI();

        // 로그인 + 회원가입 + refresh api: jwt 검사를 스킵
        if (path.equals("/api/auth/refresh")||path.equals("/api/auth/login") || path.equals("/api/auth/signup")) {
            chain.doFilter(req, res);
            return;
        }

        //토큰 없을 시 다음 필터로 이동
        if(authorization==null || !authorization.startsWith("Bearer ")){
            chain.doFilter(req,res);
            return;
        }

        //token 문자열 추출
        String token=authorization.substring(7);

        try{
            //만료가 되었는지 확인
            if(jwtUtil.isExpired(token)){
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                res.getWriter().write("{\"message\":\"Token expired\"}");
                return;
            }

            //token의 payload 에서 userId, role을 꺼냄
            //유저가 로그인이 된, 인증된 유저임을 명시
            Long userId= jwtUtil.getUserId(token);
            String hasRole= jwtUtil.getHasRole(token);

            var authorities= List.of(new SimpleGrantedAuthority(hasRole));
            var authToken=new UsernamePasswordAuthenticationToken(
                    userId, null, authorities );

            authToken.setDetails(userId);

            SecurityContextHolder.getContext().setAuthentication(authToken);
            chain.doFilter(req,res);
        }
        catch (Exception e){
            // 서명 위조/형식 오류 등
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write("{\"message\":\"Invalid token\"}");

        }
    }
}
