package com.union.demo.dto.request;

public class PingReqDto {
    private Long post_id;

    public PingReqDto() {}
    public PingReqDto(Long post_id) { this.post_id = post_id; }

    public Long getPost_id() { return post_id; }
    public void setPost_id(Long post_id) { this.post_id = post_id; }
}

