package com.union.demo.service;

import com.union.demo.dto.request.SignupReqDto;
import com.union.demo.entity.Profile;
import com.union.demo.entity.Role;
import com.union.demo.entity.University;
import com.union.demo.entity.Users;
import com.union.demo.enums.JwtRole;
import com.union.demo.repository.*;
import com.union.demo.utill.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

//jwt 기준
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ProfileRepository profileRepository;
    private final UniversityRepository universityRepository;

    //signup 회원가입
    @Transactional
    public Users signUp(SignupReqDto signUpReqDto){
        //중복 검사
        validationDuplicateLoginId(signUpReqDto.getLoginId());
        validationDuplicateUsername(signUpReqDto.getUsername());

        //user 생성, 저장
        Users user=createUserEntity(signUpReqDto);
        Users saved=userRepository.save(user);

        //user_profile 생성, 저장
        Profile profile=createProfileEntity(signUpReqDto, saved);
        profileRepository.save(profile);

        return saved;
    }

    //loginId 중복체크 409 error
    private void validationDuplicateLoginId(String loginId){
        if(userRepository.existsByLoginId(loginId)){
            log.warn("중복된 loginId입니다. {}", loginId);
            throw new IllegalStateException("이미 사용중인 아이디입니다.");
        }
    }

    //username 중복 체크 409 error
    private void validationDuplicateUsername(String username){
        if(userRepository.existsByUsername(username)){
            log.warn("중복된 username 입니다: {}", username);
            throw new IllegalStateException("이미 사용중인 이름입니다.");
        }
    }

    // users 정보 저장: user entity 생성(비밀번호 암호화 적용)
    private Users createUserEntity(SignupReqDto signupReqDto){

        //존재하지 않는 roleId -> 404
        Role mainRole=roleRepository.findByRoleId(signupReqDto.getMainRoleId())
                .orElseThrow((()-> new NoSuchElementException("존재하지 않는 roleId 입니다.")));

        return Users.builder()
                .loginId(signupReqDto.getLoginId())
                .username(signupReqDto.getUsername())
                .password(bCryptPasswordEncoder.encode(signupReqDto.getPassword()))
                .mainRoleId(mainRole)
                .jwtRole(JwtRole.USER) //기본값은 USER
                .build();
    }

    //profile 정보 저장

    private Profile createProfileEntity(SignupReqDto signupReqDto, Users user){

        University university=universityRepository.findByUnivId(signupReqDto.getUniversityId())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 대학 id 입니다."));
        return Profile.builder()
                .user(user)
                .email(signupReqDto.getEmail())
                .birthYear(signupReqDto.getBirthYear())
                .university(university)
                .major(signupReqDto.getMajor())
                .gender(signupReqDto.getGender())
                .entranceYear(signupReqDto.getEntranceYear())
                .status(signupReqDto.getStatus())
                .build();
    }

    //logout
    @Transactional
    public void logout(HttpServletRequest req, HttpServletResponse res){
        //쿠키에서 refresh token을 꺼내기
        String refreshToken=CookieUtil.getCookieValue(req, CookieUtil.REFRESH_COOKIE_NAME)
                .orElse(null);

        //db에서 refresh token 삭제
        if(refreshToken!=null && !refreshToken.isBlank()){
            refreshTokenRepository.deleteByToken(refreshToken);
        }

        //refresh 쿠키 삭제
        CookieUtil.clearRefreshCookie(res);

    }
}
