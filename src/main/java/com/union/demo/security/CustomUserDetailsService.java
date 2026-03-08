package com.union.demo.security;

import com.union.demo.entity.Users;
import com.union.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    //loginId를 이용해 사용자 정보를 조회합니다.
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException{
        Optional< Users> usersOptional=userRepository.findByLoginId(loginId);

        Users user=usersOptional.orElseThrow(()->{
            log.warn("사용자를 찾을 수 없습니다: loginId={}", loginId);
            return new UsernameNotFoundException("사용자를 찾을 수 없습니다."+loginId);
        });

        return new CustomUserDetails(user);
    }


}
