package com.union.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostMatchReqDto {

    @JsonProperty("post_id")
    private Long postId;

    public PostMatchReqDto() {}

    public PostMatchReqDto(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
