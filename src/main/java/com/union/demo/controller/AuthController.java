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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

    @RestController
    @RequestMapping("/api/auth")
    public class AuthController {

        private final AuthService authService;
        private final RefreshTokenService refreshTokenService;

        public AuthController(AuthService authService, AuthenticationManager authenticationManager, JWTUtil jWTUtil, UserRepository userRepository, RefreshTokenService refreshTokenService) {
        this.authService = authService;
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
        @Operation(
                summary = "로그인 (Swagger 문서용)",
                description = """
                    실제 로그인 처리는 LoginFilter에서 수행됩니다.
                    
                    - 요청: application/x-www-form-urlencoded (loginId, password)
                    - 응답:
                      1) Authorization 헤더에 'Bearer {accessToken}' 응답
                      2) refreshToken은 HttpOnly 쿠키(Set-Cookie)로 내려갑니다.
                    
                    - (주의) refreshToken은 HttpOnly 쿠키이므로 JS에서 직접 읽을 수 없습니다.
                    - (주의) 위의 이유로 swagger에서는 헤더에 access token만 보이고 refresh는 보이지 않습니다. 그러나 실제로는 헤더 Set-Cookie로 응답됩니다.
                    - (주의) refresh token을 확인하고 싶으시다면 <개발자도구-network 탭-"Cookies">를 확인하시길 바랍니다.
                    """
        )
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "로그인 성공",
                headers = {
                        @Header(
                                name = "Authorization",
                                description = "Bearer {accessToken}",
                                schema = @Schema(type = "string")
                        ),
                        @Header(
                                name = "Set-Cookie",
                                description = "refreshToken=...; HttpOnly=true; Secure: 로컬 개발 중에는 false, https로 배포하면 true; SameSite=None",
                                schema = @Schema(type = "string")
                        )
                }
        )
        @PostMapping(
                value = "/login",
                consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE
        )
        public ResponseEntity<ApiResponse<LoginResDto>> loginSwagger(
                @Valid @ModelAttribute LoginReqDto loginReqDto
        ) {
            // 실제 처리는 LoginFilter가 수행하므로, 이 메서드는 보통 실행되지 않습니다.
            // (혹시라도 필터가 비활성화된 환경에서 호출되면) 문서용 더미 응답을 반환합니다.
            LoginResDto dummy = new LoginResDto(null, loginReqDto.getLoginId(), "DUMMY_ACCESS_TOKEN");
            return ResponseEntity.ok(ApiResponse.ok(dummy));
        }

    //2. refresh "/api/auth/refresh"
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refresh(HttpServletRequest req, HttpServletResponse res){
        String refreshToken=extractRefreshFromCookie(req);
        RefreshTokenService.TokenPair pair= refreshTokenService.refreshAndRotate(refreshToken);

        //new refresh token을 cookie로 내려줌
        int refreshMaxAgeSeconds=60*60*24*14;
        CookieUtil.addRefreshCookie(res, pair.refreshToken(), refreshMaxAgeSeconds);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + pair.accessToken())
                .body(ApiResponse.ok());
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
            @Parameter(example = "홍길동")
            @RequestParam("q") String username
    ) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("username(q)은 필수입니다.");
        }

        UsernameResDto data = authService.checkUsernameAvailability(username.trim());
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

};
