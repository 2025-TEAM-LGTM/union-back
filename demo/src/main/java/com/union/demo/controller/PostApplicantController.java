package com.union.demo.controller;

import com.union.demo.dto.response.ApplyResDto;
import com.union.demo.global.common.ApiResponse;
import com.union.demo.service.PostApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApplicantController {
    private final PostApplicantService postApplicantService;

    //1 공고에 지원하기  "/api/posts/{postId}/applications"
    @PostMapping("/{postId}/applications")
    public ResponseEntity<ApiResponse<ApplyResDto>> apply(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId
    ){
        ApplyResDto data= postApplicantService.apply(postId, userId);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }


    //2 공고 지원 취소하기
    @DeleteMapping("/{postId}/applications/me")
    public ResponseEntity<ApiResponse<ApplyResDto>> cancelApply(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId
    ){

        ApplyResDto data= postApplicantService.cancelApply(postId, userId);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }


    //3. 공고에 지원한 팀원 목록 필터 기능  /api/posts/{postId}/applicant?roleId=...&skillIds=1,2,3&p=


}
