package com.union.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsernameResDto {
    private String username;
    private boolean isAvailable;

    public static UsernameResDto of(String username, boolean isAvailable) {
        return new UsernameResDto(username, isAvailable);
    }
}
