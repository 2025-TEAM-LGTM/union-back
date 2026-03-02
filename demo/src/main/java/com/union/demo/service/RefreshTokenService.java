package com.union.demo.service;

import com.union.demo.entity.RefreshToken;
import com.union.demo.entity.Users;
import com.union.demo.jwt.JWTUtil;
import com.union.demo.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    //만료 정책
    private static final long ACCESS_EXPIRE_MS=1000L*60*30 ;//30분
    private static final long REFRESH_EXPIRE_MS=1000L*60*60*24*14; //14일

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTUtil jwtUtil;

    //로그인 성공 시: refresh 생성, db에 저장
    @Transactional
    public RefreshToken issueRefresh(Users user){
        String refresh=jwtUtil.createJWT(
                "refresh",
                user.getUserId(),
                user.getLoginId(),
                user.getHasRole(),
                REFRESH_EXPIRE_MS
        );

        RefreshToken entity=RefreshToken.of(
                user,
                refresh,
                Instant.now().plusMillis(REFRESH_EXPIRE_MS)
        );

        return refreshTokenRepository.save(entity);
    }

    //refresh 검증 + access 재발급 + refresh 로테이션
    @Transactional
    public TokenPair refreshAndRotate(String refreshToken){
        //refresh token 검증1: jwt 자체 검증
        validateRefreshJwt(refreshToken);

        //refresh token 검증2: db에서 살아있는 refresh인지를 검증
        RefreshToken stored=refreshTokenRepository.findByTokenAndRevokedFalse(refreshToken)
                .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 refresh token입니다."));

        if(stored.isExpired(Instant.now())){
            stored.revoke();
            throw new IllegalArgumentException("만료된 refresh token입니다.");
        }

        //refresh token의 주인 꺼내기
        Users user=stored.getUser();

        //로테이션: 기존 refresh 폐기
        stored.revoke();

        //새 refresh 발급+db에 저장
        RefreshToken newRefresh=issueRefresh(user);

        //새 access Token 발급
        String newAccess=jwtUtil.createJWT(
                "access",
                user.getUserId(),
                user.getLoginId(),
                user.getHasRole(),
                ACCESS_EXPIRE_MS
        );
        return new TokenPair(newAccess, newRefresh.getToken());
    }

    public void validateRefreshJwt(String refreshToken){
        //만료 검사
        if(jwtUtil.isExpired(refreshToken)){
            throw new IllegalArgumentException("refresh token 만료");
        }

        //category 검사
        String category=jwtUtil.getCategory(refreshToken);
        if(!"refresh".equals(category)){
            throw new IllegalArgumentException("refresh token이 아닙니다.");
        }
    }

    //간단한 DTO
    public record TokenPair(String accessToken, String refreshToken){}
}
