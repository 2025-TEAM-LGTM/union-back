package com.union.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostMatchUserDto {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("main_strength")
    private String mainStrength;
}
