package com.union.demo.controller;

import com.union.demo.dto.response.MemberListResDto;
import com.union.demo.dto.response.MemberMatchResDto;
import com.union.demo.dto.response.PostMatchResDto;
import com.union.demo.enums.PersonalityKey;
import com.union.demo.global.common.ApiResponse;
import com.union.demo.service.PostMatchService;
import com.union.demo.utill.PersonalityParserUtil;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PostMatchController {
    // 공고에 핏한 팀원 목록 조회 및 필터링 "/api/posts/{postId}/matches"
    private final PostMatchService postMatchService;

    public PostMatchController(PostMatchService postMatchService) {
        this.postMatchService = postMatchService;
    }

    @GetMapping("/api/posts/{postId}/matches")
    public ResponseEntity<ApiResponse<MemberMatchResDto>> getPostMatch(
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
        Map<PersonalityKey, Integer> personalityFilter= PersonalityParserUtil.parse(personality);
        MemberMatchResDto data=postMatchService.postMatchFastApi(postId,roleIds,hardSkillIds, personalityFilter);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }



}
