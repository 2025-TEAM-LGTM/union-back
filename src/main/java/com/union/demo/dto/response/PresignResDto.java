package com.union.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PresignResDto {
    private String key;
    private String presignedUrl;
}
