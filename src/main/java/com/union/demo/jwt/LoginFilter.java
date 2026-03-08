package com.union.demo.jwt;

import com.union.demo.entity.RefreshToken;
import com.union.demo.entity.Users;
import com.union.demo.repository.UserRepository;
import com.union.demo.security.CustomUserDetails;
import com.union.demo.service.RefreshTokenService;
import com.union.demo.utill.CookieUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    // 로그인 요청 시 사용자 인증 처리
    @Override
    public org.springframework.security.core.Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {

        String loginId = req.getParameter("loginId");
        String password = req.getParameter("password");

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(loginId, password);

        // AuthenticationManager가 CustomUserDetailsService를 통해 유저 조회 + 비번검증
        return authenticationManager.authenticate(authRequest);
    }

    // 로그인 성공 시 JWT 발급
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //인증된 사용자 정보 꺼내기
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        Long userId = customUserDetails.getUser().getUserId();     // Users 엔티티의 PK
        String loginId = customUserDetails.getUser().getLoginId();    // loginId
        String hasRole = authResult.getAuthorities().iterator().next().getAuthority(); // ex) ROLE_USER

        // 토큰 생성
        String accessToken = jwtUtil.createJWT(
                "access",
                userId,
                loginId,
                hasRole,
                60 * 60 * 1000L // 1시간동안 유지
        );

        //refresh 발급+db 저장
        Users user= userRepository.findById(userId).orElseThrow();
        RefreshToken saved=refreshTokenService.issueRefresh(user);

        //refresh를 http-only 쿠키로 저장
        int refreshMaxAgeSeconds=60*60*24*14;
        CookieUtil.addRefreshCookie(res, saved.getToken(), refreshMaxAgeSeconds);

        // header의 Authorization에 access token 응답
        res.setHeader("Authorization", "Bearer " + accessToken);
  }

    // 로그인 실패 시 401 응답 반환
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized 응답
    }

}





