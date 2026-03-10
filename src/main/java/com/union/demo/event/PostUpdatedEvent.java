package com.union.demo.event;

public class PostUpdatedEvent {
    private final Long postId;

    public PostUpdatedEvent(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }
}
