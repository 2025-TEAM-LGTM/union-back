package com.union.demo.security;

import com.union.demo.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    //users 엔티티를 spring security가 이해하는 사용자 정보로 변환

    private final Users user;

    // 권한 정보 (지금은 기본 USER 권한만)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // 비밀번호: 암호화된 값
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 로그인아이디
    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 활성화여부
    @Override
    public boolean isEnabled() {
        return true;
    }

    // users 접근용
    public Users getUser() {
        return user;
    }
}


