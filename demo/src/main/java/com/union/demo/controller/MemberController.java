package com.union.demo.controller;

import com.union.demo.dto.response.MemberListResDto;
import com.union.demo.dto.response.PortfolioDetailResDto;
import com.union.demo.dto.response.PortfolioListResDto;
import com.union.demo.dto.response.ProfileResDto;
import com.union.demo.enums.PersonalityKey;
import com.union.demo.global.common.ApiResponse;
import com.union.demo.service.MemberService;
import com.union.demo.utill.PersonalityParserUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    //1. 전체 팀원 목록 조회 + 필터링 /api/members?r=105&r=103&hs=121&p=D,E
    @GetMapping
    public ResponseEntity<ApiResponse<MemberListResDto>> getMembers(
            @RequestParam(required = false, name="r") List<Integer> roleIds,
            @RequestParam(required = false, name="hs") List<Integer> hardSkillIds,
            @RequestParam(required = false, name="p") String personality
            ){
        Map<PersonalityKey, Integer> personalityFilter = PersonalityParserUtil.parse(personality);
        MemberListResDto data=memberService.getMembers(roleIds, hardSkillIds, personalityFilter);

        return ResponseEntity.ok(ApiResponse.ok(data));

        }

    //2. 팀원 프로필 보기  /api/members/{memberId}/profile
    @GetMapping("/{memberId}/profile")
    public ResponseEntity<ApiResponse<ProfileResDto>> getMemberProfile(
            @PathVariable(required = true) Long memberId
    ){
        ProfileResDto data=memberService.getMemberProfile(memberId);
        return ResponseEntity.ok(ApiResponse.ok(data));

    }

    //3. 팀원의 포트폴리오 리스트 보기
    @GetMapping("/{memberId}/portfolio")
    public ResponseEntity<ApiResponse<PortfolioListResDto>> getMemberPortfolioList(
            @PathVariable(required = true) Long memberId
    ){
        PortfolioListResDto data=memberService.getMemberPortfolioList(memberId);
;
    return ResponseEntity.ok(ApiResponse.ok(data));
    };



    //4. 팀원의 포트폴리오 상세 보기
    @GetMapping("/{memberId}/portfolio/{portfolioId}")
    public ResponseEntity<ApiResponse<PortfolioDetailResDto>> getMemberPortfolioDetail(
            @PathVariable(required = true) Long memberId,
            @PathVariable(required = true) Long portfolioId
    ){
        PortfolioDetailResDto data= memberService.getMemberPortfolioDetail(memberId, portfolioId);
        return ResponseEntity.ok(ApiResponse.ok(data));

    }


}
