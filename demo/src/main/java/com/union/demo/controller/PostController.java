package com.union.demo.controller;

import com.union.demo.dto.request.PostCreateReqDto;
import com.union.demo.dto.response.PostCreateResDto;
import com.union.demo.dto.response.PostListResDto;
import com.union.demo.entity.Users;
import com.union.demo.global.common.ApiResponse;
import com.union.demo.repository.UserRepository;
import com.union.demo.security.CustomUserDetails;
import com.union.demo.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    //1. 전체 공고 목록 조회 + 쿼리 검색
    private final PostService postService;
    private final UserRepository userRepository;

    @Operation(summary = "전체 공고 목록 조회", description = "전체 목록 조회 + 쿼리를 통해 검색도 가능, 자세한 쿼리내용은 노션 api 문서 확인하기")
    @GetMapping
    public PostListResDto getPosts(
            @RequestParam(required = false) List<Integer> domainIds,
            @RequestParam(required = false) List<Integer> fieldIds,
            @RequestParam(required = false) List<Integer> roleIds

    ){
        return postService.getAllPosts(domainIds, fieldIds, roleIds);
    }

    //2. 공고 작성 기능 "/api/posts"
    @PostMapping
    public ResponseEntity<PostCreateResDto> createPost(
            @AuthenticationPrincipal String loginId,
            @RequestBody @Valid PostCreateReqDto req
            ){
        Users leader=userRepository.findByLoginId(loginId)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        Long leaderId=leader.getUserId();

        PostCreateResDto res=postService.createPost(leaderId,req);
        return ResponseEntity.ok(res);
    }

    //3. 공고 수정하기 "/api/posts/{postId}"


    //4. 공고 삭제하기 "/api/posts/{postId}"
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Long>> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok(ApiResponse.ok(postId));
    }

    //5. 공고 상세 페이지 + 공고명 보기 "/api/posts/{postId}"







}

