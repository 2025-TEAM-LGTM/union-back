package com.union.demo.controller;

import com.union.demo.dto.request.LoginReqDto;
import com.union.demo.dto.request.SignupReqDto;
import com.union.demo.dto.response.LoginResDto;
import com.union.demo.dto.response.SignupResDto;
import com.union.demo.dto.response.UsernameResDto;
import com.union.demo.entity.Users;
import com.union.demo.global.common.ApiResponse;
import com.union.demo.jwt.JWTUtil;
import com.union.demo.repository.UserRepository;
import com.union.demo.service.AuthService;
import com.union.demo.service.RefreshTokenService;
import com.union.demo.utill.CookieUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@SecurityRequirement(name = "JWT")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jWTUtil;
    private final UserRepository userRepository;
    private static final long ACCESS_TOKEN_EXPIRED_MS = 30 * 60 * 1000L; // 30분
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JWTUtil jWTUtil, UserRepository userRepository, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jWTUtil = jWTUtil;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    //1. 회원가입 "/api/auth/signup"
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResDto>> signUp(@RequestBody @Valid SignupReqDto signupReqDto){
        Users savedUser=authService.signUp(signupReqDto);
        Long userId=savedUser.getUserId();
        String loginId=savedUser.getLoginId();
        String username=savedUser.getUsername();

        SignupResDto data=new SignupResDto(
                userId,
                loginId,
                username
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(data));
    }

    //2. 로그인 "/api/auth/login"

    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity <ApiResponse<LoginResDto>> login(@RequestBody @Valid LoginReqDto loginReqDto){
        //아이디 비번 검증
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginReqDto.getLoginId(),
                        loginReqDto.getPassword()
                )
        );

        Users user=userRepository.findByLoginId(loginReqDto.getLoginId())
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 loginId입니다."));

        Long userId= user.getUserId();
        String loginId=user.getLoginId();

        String hasRole="ROLE_USER";

        //JWT 생성
        String accessToken= jWTUtil.createJWT(
                "access",userId, user.getLoginId(),hasRole,
                ACCESS_TOKEN_EXPIRED_MS
        );
        // 헤더에도 넣어주기
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        // 바디 응답
        LoginResDto data=new LoginResDto(
                userId, loginId, accessToken
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(data));
    }

    //refresh
    @PostMapping("/refresh")
    public Map<String, String> refresh(HttpServletRequest req, HttpServletResponse res){
        String refreshToken=extractRefreshFromCookie(req);
        RefreshTokenService.TokenPair pair= refreshTokenService.refreshAndRotate(refreshToken);

        //refresh 쿠키 갱신
        int refreshMaxAgeSeconds=60*60*24*14;
        CookieUtil.addRefreshCookie(res, pair.refreshToken(), refreshMaxAgeSeconds);

        //access 내려주기
        return Map.of("accessToken", pair.accessToken());
    }

    private String extractRefreshFromCookie(HttpServletRequest req){
        Cookie[] cookies=req.getCookies();
        if(cookies==null) throw new IllegalArgumentException("쿠키가 없습니다.");

        return Arrays.stream(cookies)
                .filter(c-> CookieUtil.REFRESH_COOKIE_NAME.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(()-> new IllegalArgumentException("refresh 쿠키가 없습니다."));
    }

    //3. 로그아웃 "/api/auth/logout"
    @GetMapping ("/logout")
    public ResponseEntity<ApiResponse<?>> logout (HttpServletRequest req, HttpServletResponse res){
        authService.logout(req,res);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok());

    }


    //4. username 중복 검사 "api/auth/nickname?q=홍길동"
    @GetMapping("/username")
    public ResponseEntity<ApiResponse<UsernameResDto>> checkUsername(
            @RequestParam("q") String username
    ) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("username(q)은 필수입니다.");
        }

        UsernameResDto data = authService.checkUsernameAvailability(username.trim());
        return ResponseEntity.ok(ApiResponse.ok(data));
    }


};
