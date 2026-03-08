package com.union.demo.controller;

import com.union.demo.dto.response.ApplyResDto;
import com.union.demo.dto.response.MemberListResDto;
import com.union.demo.enums.PersonalityKey;
import com.union.demo.global.common.ApiResponse;
import com.union.demo.service.MemberService;
import com.union.demo.service.PostApplicantService;
import com.union.demo.utill.PersonalityParserUtil;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApplicantController {
    private final PostApplicantService postApplicantService;
    private final MemberService memberService;

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


    //3. 공고에 지원한 팀원 목록 필터 기능  /api/posts/{postId}/applicant?r=105&hs=121&p=D
    @GetMapping("/{postId}/applicants")
    public ResponseEntity<ApiResponse<MemberListResDto>> getApplicants(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @Parameter(example = "[101,102]")
            @RequestParam(required = false, name="r") List<Integer> roleIds,
            @Parameter(example = "[121,122]")
            @RequestParam(required = false, name="hs") List<Integer> hardSkillIds,
            @Parameter(example = """
                    {"A": 1, "B": 1}
                    """)
            @RequestParam(required = false, name="p") String personality
    ){
        //personality 파싱(String -> key)
        Map<PersonalityKey, Integer> personalityMap=
                PersonalityParserUtil.parse(personality);

        MemberListResDto data=memberService.getApplicants(userId, postId,roleIds,hardSkillIds, personalityMap);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

}
