package com.union.demo.service;

import com.union.demo.dto.response.ApplyResDto;
import com.union.demo.entity.Applicant;
import com.union.demo.entity.Post;
import com.union.demo.entity.Users;
import com.union.demo.enums.RecruitStatus;
import com.union.demo.repository.ApplicantRepository;
import com.union.demo.repository.PostRepository;
import com.union.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostApplicantService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ApplicantRepository applicantRepository;

    //1. applyPost 공고지원하기
    @Transactional
    public ApplyResDto apply(Long postId, Long userId){
        //유저 확인
        Users user=userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //공고 확인
        Post post=postRepository.findByPostId(postId)
                .orElseThrow(()-> new NoSuchElementException("해당 공고를 찾을 수 없습니다."));

        //마감 공고 지원
        if(post.getPostInfo().getStatus()!=RecruitStatus.OPEN) {
            throw new IllegalStateException("마감된 공고에는 지원할 수 없습니다.");
        }

        //본인 공고에는 지원 불가
        if(post.getLeaderId().getUserId().equals(userId)){
            throw new IllegalStateException("본인 공고에는 지원할 수 없습니다.");
        }


        //중복 지원 체크
        if(applicantRepository.existsByPost_PostIdAndUser_UserId(postId, userId)){
            throw new IllegalStateException("이미 지원한 공고입니다.");
        }

        //지원하기
        Applicant applicant=new Applicant(
                null, post, user, OffsetDateTime.now()
        );

        Applicant saved=applicantRepository.save(applicant);

        return ApplyResDto.from(saved);

    }

    //2 공고 지원 취소하기
    @Transactional
    public ApplyResDto cancelApply(Long postId, Long userId){
        Applicant applicant=applicantRepository.findByPost_PostIdAndUser_UserId(postId, userId)
                .orElseThrow(()-> new NoSuchElementException("지원 내역이 없습니다"));

        ApplyResDto res=ApplyResDto.from(applicant);
        applicantRepository.delete(applicant);
        return res;

    }

    //3. getApplicants  공고에 지원한 사람들 조회 + 필터링



}
