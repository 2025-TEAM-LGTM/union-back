package com.union.demo.dto.response;

public class PingResDto {
    private boolean ok;
    private Long received_post_id;
    private String message;

    public boolean isOk() { return ok; }
    public void setOk(boolean ok) { this.ok = ok; }

    public Long getReceived_post_id() { return received_post_id; }
    public void setReceived_post_id(Long received_post_id) { this.received_post_id = received_post_id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
