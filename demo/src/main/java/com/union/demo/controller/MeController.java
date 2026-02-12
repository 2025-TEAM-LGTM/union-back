package com.union.demo.controller;

import com.union.demo.dto.request.PortfolioPostReqDto;
import com.union.demo.dto.request.ProfileUpdateReqDto;
import com.union.demo.dto.response.MyProfileResDto;
import com.union.demo.dto.response.PortfolioDetailResDto;
import com.union.demo.dto.response.PortfolioListResDto;
import com.union.demo.global.common.ApiResponse;
import com.union.demo.repository.UserRepository;
import com.union.demo.service.MeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me")
public class MeController {
    private final MeService meService;
    private final UserRepository userRepository;

    //[1. 프로필]
    //1.1 프로필 정보 가져오기 /api/me/profile
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<MyProfileResDto>> getMyProfile(
            @AuthenticationPrincipal Long userId
    ){
        MyProfileResDto data= meService.getMyProfile(userId);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    //1.2 프로필 정보 수정하기 /api/me/profile
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<MyProfileResDto>> updateMyProfile(
            @AuthenticationPrincipal Long userId,
            @RequestBody ProfileUpdateReqDto profileUpdateReqDto
    ){
        MyProfileResDto data= meService.updateMyProfile(userId, profileUpdateReqDto);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }


    //[2. 포트폴리오]
    //2.1 포트폴리오 목록 가져오기 /api/me/portfolios
    @GetMapping("/portfolio")
    public ResponseEntity<ApiResponse<PortfolioListResDto>> getPortfolioList(
            @AuthenticationPrincipal Long userId
    ){
        PortfolioListResDto data= meService.getPortfolioList(userId);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }


    //2.2 포트폴리오 업로드하기 /api/me/portfolios/{portfolioId}
    @PostMapping("/portfolio")
    public ResponseEntity<ApiResponse<PortfolioDetailResDto>> createPortfolio(
            @AuthenticationPrincipal Long userId,
            @RequestBody PortfolioPostReqDto req
            )
    {
        PortfolioDetailResDto data=meService.createPortfolio(userId, req);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }


    //2.3 포트폴리오 세부 페이지 /api/me/portfolios/{portfolioId}
    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<ApiResponse<PortfolioDetailResDto>> getPortfolioDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long portfolioId
    ){
        PortfolioDetailResDto data=meService.getPortfolioDetail(userId, portfolioId);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }


    //2.4 포트폴리오 삭제하기 /api/me/portfolios/{portfolioId}
    @DeleteMapping("/portfolio/{portfolioId}")
    public ResponseEntity<ApiResponse<?>> deletePortfolio(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long portfolioId
    ){
        meService.deletePortfolio(userId, portfolioId);
        return ResponseEntity.ok(ApiResponse.ok());
    }


    //2.5 포트폴리오 수정하기 /api/me/portfolios/{portfolioId}
}
