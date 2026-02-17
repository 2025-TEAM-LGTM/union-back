package com.union.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostMatchUserDto {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("main_strength")
    private String strength;

    public PostMatchUserDto() {}

    public Long getUserId() {
        return userId;
    }

    public String getStrength() {
        return strength;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }
}
