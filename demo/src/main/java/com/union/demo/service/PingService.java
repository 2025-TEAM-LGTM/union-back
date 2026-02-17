package com.union.demo.service;

import com.union.demo.dto.request.PingReqDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PingService {

    private final RestTemplate restTemplate;

    public PingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public com.union.demo.dto.response.PingResDto pingFastApi(Long postId) {
        String url = "http://localhost:8000/ping";
        PingReqDto req = new PingReqDto(postId);

        return restTemplate.postForObject(url, req, com.union.demo.dto.response.PingResDto.class);
    }
}

