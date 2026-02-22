package com.union.demo.service;

import com.union.demo.dto.response.*;
import com.union.demo.entity.*;
import com.union.demo.enums.PersonalityKey;
import com.union.demo.repository.*;
import com.union.demo.utill.S3UrlResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService{
    private final MemberRepository memberRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PortfolioRepository portfolioRepository;
    private final ApplicantRepository applicantRepository;
    private final PostRepository postRepository;
    private final S3UrlResolver s3UrlResolver;

    //멤버 조회(공통 로직)
    public MemberListResDto getMembersInternal(
            List<Long> baseUserIds,
            List<Integer> roleIds,
            List<Integer> hardSkillIds,
            Map<PersonalityKey, Integer> personality
    ){
        List<Users> users= memberRepository.searchMembers(baseUserIds,roleIds,hardSkillIds,personality);
        if(users.isEmpty()){
            return MemberListResDto.builder()
                    .members(Collections.emptyList())
                    .build();
        }

        //userId
        List<Long> userIds = users.stream().map(Users::getUserId).toList();

        //userSkill
        List<UserSkill> userSkills = userSkillRepository.findAllByUserIdInWithSkill(userIds);
        //userId를 기준으로 skill 들을 묶는 것
        Map<Long, List<MemberListResDto.ItemDto>> skillsByUserId = userSkills.stream()
                .collect(Collectors.groupingBy(
                        us -> us.getUser().getUserId(),
                        Collectors.mapping(
                                us -> MemberListResDto.ItemDto.builder()
                                        .id(us.getSkill().getSkillId())
                                        .name(us.getSkill().getSkillName())
                                        .build(),
                                Collectors.toList())
                ));

        //dto 최종 매핑
        //userId로 skill를 찾아보고 skill이 만약 없다면 빈 리스트로 저장
        List<MemberListResDto.MemberDto> memberDtos=users.stream()
                .map(user-> MemberListResDto.from(user,
                        skillsByUserId.getOrDefault(user.getUserId(),List.of()),
                        s3UrlResolver
                ))
                .toList();

        return MemberListResDto.builder()
                .members(memberDtos)
                .build();

    }

    // 전체 멤버 리스트 조회 및 필터링
    public MemberListResDto getMembers(
            List<Integer> roleIds,
            List<Integer> hardSkillIds,
            Map<PersonalityKey, Integer> personality
    ) {
        return getMembersInternal(null, roleIds, hardSkillIds, personality);

    }

    //공고에 지원한 지원자들 리스트 조회 및 필터링
    public MemberListResDto getApplicants(
            Long userId,
            Long postId,
            List<Integer> roleIds,
            List<Integer> hardSkillIds,
            Map<PersonalityKey, Integer> personality
    ){
        Post post=postRepository.findByPostId(postId)
                .orElseThrow(()-> new NoSuchElementException("해당 공고를 찾을 수 없습니다."));

        //공고 주인 = 현재 로그인한 사람
        //403 오류!
        if(!post.getLeaderId().getUserId().equals(userId)){
            throw new IllegalArgumentException("공고 지원자 리스트를 조회할 권한이 없습니다.");
        }

        List<Long> applicantUserIds=applicantRepository.findApplicantUserId(postId);

        if(applicantUserIds.isEmpty()){
            return MemberListResDto.builder()
                    .members(Collections.emptyList())
                    .build();
        }
        return getMembersInternal(applicantUserIds, roleIds,hardSkillIds,personality);
    }

    //공고 매칭 팀원 리스트 필터링
    public MemberMatchResDto getMatchMembers(
            List<Long> matchingUserIds,
            Map<Long, String> strengthMap,
            List<Integer> roleIds,
            List<Integer> hardSkillIds,
            Map<PersonalityKey, Integer> personality
    ){

        //추천 후보군 안에서만 필터링 로직
        List<Users> users=memberRepository.searchMembers(matchingUserIds,roleIds,hardSkillIds,personality);

        if(users.isEmpty()){
            return MemberMatchResDto.builder()
                    .members(Collections.emptyList())
                    .build();
        }

        //userId
        List<Long> usersIds=users.stream().map(Users::getUserId).toList();

        //userSkill
        List<UserSkill> userSkills= userSkillRepository.findAllByUserIdInWithSkill(usersIds);

        //userId 기준으로 skills 그룹핑
        Map<Long, List<MemberMatchResDto.ItemDto>> skillsByUserId=userSkills.stream()
                .collect(Collectors.groupingBy(
                        us-> us.getUser().getUserId(),
                        Collectors.mapping(
                                us-> MemberMatchResDto.ItemDto.builder()
                                        .id(us.getSkill().getSkillId())
                                        .name(us.getSkill().getSkillName())
                                        .build(),
                                Collectors.toList()
                        )
                ));

        //fastAPI 추천 순서 유지
        Map<Long, Users> userMap=users.stream()
                .collect(Collectors.toMap(Users::getUserId, u->u));

        //추천 순서대로 dto + mainStrength 구성
        List<MemberMatchResDto.MemberMatchDto> memberDtos= matchingUserIds.stream()
                .map(userMap::get)
                .filter(Objects::nonNull)
                .map(user -> MemberMatchResDto.from(
                        user,
                        skillsByUserId.getOrDefault(user.getUserId(),List.of()),
                        strengthMap!=null ? strengthMap.get(user.getUserId()): null,
                        s3UrlResolver
                ))
                .toList();

        return MemberMatchResDto.builder()
                .members(memberDtos)
                .build();
    }

    // getMemberProfile 함수
    public ProfileResDto getMemberProfile(Long memberId){
        Users user=userRepository.findByUserId(memberId)
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Profile profile=profileRepository.findByUserId(memberId)
                .orElseThrow(()-> new NoSuchElementException("프로필을 찾을 수 없습니다."));

        return ProfileResDto.from(user, profile,s3UrlResolver);

    }

    //getMemberPortfolioList 함수: 팀원 포폴 리스트 조회
    public PortfolioListResDto getMemberPortfolioList(Long memberId){
        userRepository.findByUserId(memberId)
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Portfolio> portfolio=portfolioRepository.findPortfolioByUserId(memberId);

        return PortfolioListResDto.from(portfolio,s3UrlResolver);
    }

    //getMemberPortfolioDetail: 팀원의 상세 포트폴리오 조회
    public PortfolioDetailResDto getMemberPortfolioDetail(Long memberId, Long portfolioId){
       userRepository.findByUserId(memberId)
               .orElseThrow(()-> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        Portfolio portfolio=portfolioRepository.findDetailByPortfolioId(portfolioId)
                .orElseThrow(()-> new NoSuchElementException("해당 포트폴리오를 찾을 수 없습니다."));

        if(!portfolio.getUser().getUserId().equals(memberId)){
            throw new AccessDeniedException("포트폴리오 작성자의 id와 memberId가 서로 다릅니다.");
        }
        return PortfolioDetailResDto.from(portfolio,s3UrlResolver);
    }
}
