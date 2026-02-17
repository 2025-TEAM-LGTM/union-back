package com.union.demo.service;

import com.union.demo.dto.request.PostMatchReqDto;
import com.union.demo.dto.response.PostMatchResDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostMatchService {

    //1. getMatching 팀원 추천받기 + 필터링
    private final RestTemplate restTemplate;

    public PostMatchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PostMatchResDto postMatchFastApi(Long postId) {
        String url = "http://localhost:8000/match_result";
        PostMatchReqDto req = new PostMatchReqDto(postId);

        return restTemplate.postForObject(url, req, PostMatchResDto.class);
    }


    //2. getPersonalityMatch  성향일치 계산하기

}
