package com.union.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResDto {
    private Long userId;
    private String loginId;
    private String username;
}
