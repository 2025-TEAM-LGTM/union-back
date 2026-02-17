package com.union.demo.controller;

import com.union.demo.dto.response.PostMatchResDto;
import com.union.demo.service.PostMatchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostMatchController {
    //1. 공고에 핏한 팀원 목록 보기 "/api/posts/{postId}/matches"
    //성향 일치 정보는 service에서 이루어짐
    private final PostMatchService postMatchService;

    public PostMatchController(PostMatchService postMatchService) {
        this.postMatchService = postMatchService;
    }

    @GetMapping("/api/posts/{postId}/matches")
    public PostMatchResDto getPostMatch(@PathVariable Long postId){
        return postMatchService.postMatchFastApi(postId);
    }


    //2. 공고에 핏한 팀원 필터기능 "/api/posts/123/matches?roleId=...&skillIds=1,2,3&p= "
    // @GetMapping(".api/posts/{postId}/matches?role=Id")


}
