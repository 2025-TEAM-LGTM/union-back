package com.union.demo.service;

import com.union.demo.dto.request.PostCreateReqDto;
import com.union.demo.dto.request.PostUpdateReqDto;
import com.union.demo.dto.response.PostDetailResDto;
import com.union.demo.dto.response.PostListResDto;
import com.union.demo.dto.response.PostPageResDto;
import com.union.demo.entity.*;
import com.union.demo.enums.RecruitStatus;
import com.union.demo.enums.TeamCultureKey;
import com.union.demo.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostInfoRepository postInfoRepository;
    private final RoleRepository roleRepository;
    private final DomainRepository domainRepository;
    private final PostRecruitRoleRepository postRecruitRoleRepository;
    private final PostCurrentRoleRepository postCurrentRoleRepository;

    private static final ZoneOffset KST=ZoneOffset.ofHours(9);

    //1. getAllPosts 전체 공고 목록 조회 * 공고 필터기능
    public PostListResDto getAllPosts(List<Integer> domainIds, List<Integer> fieldIds, List<Integer> roleIds){
        //빈 리스트 처리
        domainIds=normalize(domainIds);
        fieldIds=normalize(fieldIds);
        roleIds=normalize(domainIds);


        var posts=postRepository.findAllWithInfoAndRecruitRoles( domainIds,  fieldIds,  roleIds);

        var postDtos=posts.stream().map(p-> {
            //D-day 계산
            int dday=calcDday(p.getPostInfo().getRecruitEdate());
            //만약 dday 입력 안되었을 때 처리
            //int dday = calcDday(p.getPostInfo() != null ? p.getPostInfo().getRecruitEdate() : null);

            //recruits 정보 빌드
            var recruits= p.getRecruitRoles().stream()
                    .map(prr -> PostListResDto.RecruitDto.builder()
                            .roleId(prr.getRole().getRoleId())
                            .roleName((prr.getRole().getRoleName()))
                            .roleCount((prr.getCount())) //만약 count가 없을 때 prr.getCount() == null ? 0 : prr.getCount()
                            .build()
                    )
                    .toList();

            //domainIds 만들기
            var domainIdList=toDomainIds(p);

            //postList 데이터들 빌드
            return PostListResDto.PostSummaryDto.builder()
                    .postId(p.getPostId())
                    .title(p.getTitle())
                    .dday(dday)
                    .domainIds(domainIdList)
                    .recruits(recruits)
                    .build();
        }).toList();

        return PostListResDto.builder()
                .posts(postDtos)
                .build();
    }
    //빈 리스트면 null 처리
    private List<Integer> normalize(List<Integer> ids) {
        return (ids == null || ids.isEmpty()) ? null : ids;
    }
    //D-day 계산
    private int calcDday(OffsetDateTime recruitEdate){
        if(recruitEdate == null) return 0;
        return (int) ChronoUnit.DAYS.between(OffsetDateTime.now(), recruitEdate);
    }

    //domainId 가져오기
    private List<Integer> toDomainIds(Post post){
        return java.util.stream.Stream.of(post.getPrimeDomainId(),post.getSecondDomainId())
                .filter(java.util.Objects :: nonNull)
                .map(Domain::getDomainId)
                .distinct()
                .toList();
    }



    //2. createPost 공고 작성
    @Transactional
    public PostDetailResDto createPost(Long leaderId, PostCreateReqDto req){

        LocalDate start=req.getRecruitPeriod().getStartDate();
        LocalDate end=req.getRecruitPeriod().getEndDate();

        //모집 날짜 오류 잡기
        if(end.isBefore(start)){
            throw new IllegalArgumentException("모집이 끝나는 시간이 시작하는 날짜보다 늦어야 합니다.");
        }

        //leader 조회하기
        Users leader =userRepository.findByUserId(leaderId)
                .orElseThrow(()-> new IllegalArgumentException("leaderId를 가진 사용자를 찾을 수 없습니다."));

        //domain 검증
        List<Integer> domainIds=Optional.ofNullable(req.getDomainIds())
                .orElseThrow(()-> new IllegalArgumentException("domain을 입력해야합니다."));

        Integer primeDomainId=domainIds.get(0);
        Integer secondDomainId = domainIds.size() >= 2 ? domainIds.get(1) : null;

        //도메인 조회
        Domain primeDomain=domainRepository.findByDomainId(primeDomainId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않은 domainId입니다." + primeDomainId));
        Domain secondDomain=null;
        if(secondDomainId!=null){
            secondDomain=domainRepository.findByDomainId(secondDomainId)
                    .orElseThrow(()-> new IllegalArgumentException("존재하지 않은 domainId입니다." +secondDomainId));
        }

        //post entity 생성 및 저장
        Post post=Post.builder()
                .leaderId(leader)
                .title(req.getTitle())
                .primeDomainId(primeDomain)
                .secondDomainId(secondDomain)
                .build();

        Post savedPost=postRepository.save(post);

        //postInfo entity 생성 및 저장
        OffsetDateTime recruitSdate = start.atStartOfDay().atOffset(KST);
        OffsetDateTime recruitEdate = end.atTime(23, 59, 59).atOffset(KST);

        //일단 image는 아직 개발이 안되서 null 처리 할게요
        Image image=null;

        //teamCulture를 jsonb로 변환하기
        Map<TeamCultureKey, Integer> teamCulture=null;
        if(req.getTeamCulture()!=null){
            teamCulture=new EnumMap<>(TeamCultureKey.class);
            for(Map.Entry<String, Integer> e: req.getTeamCulture().entrySet()){
                try{
                    TeamCultureKey key=TeamCultureKey.valueOf(e.getKey());
                    teamCulture.put(key, e.getValue());
                }
                catch (IllegalArgumentException ex){
                    throw  new IllegalArgumentException("teamCulture 키가 유효하지 않습니다."+ e.getKey());
                }
            }
        }

        //postInfo 저장
        PostInfo postInfo=PostInfo.builder()
                .post(savedPost)
                .status(RecruitStatus.OPEN)
                .contact(req.getContact())
                .recruitSdate(recruitSdate)
                .recruitEdate(recruitEdate)
                .contact(req.getContact())
                .homepageUrl(req.getHomepageUrl())
                .seeking(req.getSeeking())
                .aboutUs(req.getAboutUs())
                .teamCulture(teamCulture)
                .image(image)
                .build();

        postInfoRepository.save(postInfo);

        //currentRole 저장
        List<PostCreateReqDto.RoleCountDto> currentRoles=Optional.ofNullable(req.getCurrentRoles())
                .orElseThrow(()-> new IllegalArgumentException("currentRoles를 찾을 수 없습니다."));

        if(currentRoles.isEmpty()){
            throw new IllegalArgumentException("currentRoles를 최소 한개 이상 작성하셔야합니다.");
        }

        List<PostCurrentRole> currentRoleEntities=new ArrayList<>();
        for(PostCreateReqDto.RoleCountDto roles:currentRoles){
            Integer roleId=roles.getRoleId();
            Integer count=roles.getCount();

            Role role=roleRepository.findByRoleId(roleId)
                    .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 role Id입니다."));

            PostCurrentRole entity=PostCurrentRole.builder()
                    .post(savedPost)
                    .role(role)
                    .count(count)
                    .build();
            currentRoleEntities.add(entity);
        }
        postCurrentRoleRepository.saveAll(currentRoleEntities);

        //recruitRole 저장
        List<PostCreateReqDto.RoleCountDto> recruitRoles=Optional.ofNullable(req.getCurrentRoles())
                .orElseThrow(()-> new IllegalArgumentException("recruitRoles 찾을 수 없습니다."));

        if(recruitRoles.isEmpty()){
            throw new IllegalArgumentException("recruitRoles 최소 한개 이상 작성하셔야합니다.");
        }

        List<PostRecruitRole> recruitRoleEntities=new ArrayList<>();
        for(PostCreateReqDto.RoleCountDto roles:recruitRoles){
            Integer roleId=roles.getRoleId();
            Integer count=roles.getCount();

            Role role=roleRepository.findByRoleId(roleId)
                    .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 role Id입니다."));

            PostRecruitRole entity=PostRecruitRole.builder()
                    .post(savedPost)
                    .role(role)
                    .count(count)
                    .build();
            recruitRoleEntities.add(entity);
        }
        postRecruitRoleRepository.saveAll(recruitRoleEntities);

        //response 만들기
        return PostDetailResDto.builder()
                .postId(savedPost.getPostId())
                .leaderId(leader.getUserId())
                .title(savedPost.getTitle())
                .build();
    }



    //3. updatePost 공고 수정
    @Transactional
    public PostDetailResDto updatePost(Long postId, PostUpdateReqDto req){
        //공고 조회
        Post post=postRepository.findByPostId(postId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 postId 입니다."+ postId));
        PostInfo postInfo=postInfoRepository.findByPostPostId(postId)
                .orElseThrow(()-> new IllegalArgumentException("postInfo를 찾을 수 없습니다."));

        //수정할 권한이 있는지(작성자==로그인한 사람)
        //security util 함수를 쓸 수 있는지 확인해보자
        Long userId= (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if(!post.getLeaderId().getUserId().equals(userId)){
            throw new AccessDeniedException("post update 권한이 없습니다.");
            //403 오류를 내려줘야하는데, globalExceptionHandler가 안먹음
        }

        //update
        if(req.getTitle()!=null) post.updateTitle(req.getTitle());
        if(req.getHomepageUrl()!=null) postInfo.updateHomePageUrl(req.getHomepageUrl());
        if(req.getSeeking()!=null) postInfo.updateSeeking(req.getSeeking());
        if(req.getAboutUs()!=null) postInfo.updateAboutUs(req.getAboutUs());
        if(req.getContact()!=null) postInfo.updateContact(req.getContact());
        if(req.getImageUrl()!=null) postInfo.updateImage(req.getImageUrl());

        //recruitPeriod update
        if(req.getRecruitPeriod()!=null){
            ZoneOffset KST=ZoneOffset.ofHours(9);

            if(req.getRecruitPeriod().getStartDate()!=null){
                OffsetDateTime start=req.getRecruitPeriod()
                        .getStartDate()
                        .atStartOfDay()
                        .atOffset(KST);
                postInfo.updateRecruitSdate(start);
            }
            if(req.getRecruitPeriod().getEndDate()!=null){
                OffsetDateTime end=req.getRecruitPeriod()
                        .getEndDate()
                        .atTime(23,59,59)
                        .atOffset(KST);
                postInfo.updateRecruitEdate(end);
            }
        }

        //domainId update
        if(req.getDomainIds()!=null){
            if(req.getDomainIds().get(0)!=null){
                Domain prime=domainRepository.findByDomainId(req.getDomainIds().get(0))
                        .orElseThrow(()->new IllegalArgumentException("존재하지 않는 domainInd입니다"));
                post.updatePrimeDomainId(prime);
            }
            if(req.getDomainIds().get(1)!=null){
                Domain second=domainRepository.findByDomainId(req.getDomainIds().get(1))
                        .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 domainId입니다."));
                post.updateSecondDomainId(second);
            }
        }

        //currentRole update
        if(req.getCurrentRoles()!=null){
            //기존 모집 역할들 제거
            postCurrentRoleRepository.deleteByPost(post);

            //새로 등록
            for(PostUpdateReqDto.RoleCountDto dto: req.getCurrentRoles()){
                Role role =roleRepository.findByRoleId(dto.getRoleId())
                        .orElseThrow(()->new IllegalArgumentException("존재하지 않는 roleId입니다."));

                PostCurrentRole pcr=PostCurrentRole.builder()
                        .post(post)
                        .role(role)
                        .count(dto.getCount())
                        .build();

                postCurrentRoleRepository.save(pcr);
            }
        }

        // recruitRole update
        if(req.getRecruitRoles()!=null){
            //기존 모집 역할들 제거
            postRecruitRoleRepository.deleteByPost(post);

            //새로 등록
            for(PostUpdateReqDto.RoleCountDto dto: req.getRecruitRoles()) {
                Role role = roleRepository.findByRoleId(dto.getRoleId())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 roleId입니다."));

                PostRecruitRole prr = PostRecruitRole.builder()
                        .post(post)
                        .role(role)
                        .count(dto.getCount())
                        .build();

                postRecruitRoleRepository.save(prr);
            }

        }

        //teamCulture
        if(req.getTeamCulture()!=null) {
            Map<TeamCultureKey, Integer> converted = new EnumMap<>(TeamCultureKey.class);
            for (Map.Entry<String, Integer> entry : req.getTeamCulture().entrySet()) {
                try {
                    TeamCultureKey key = TeamCultureKey.valueOf(entry.getKey());
                    converted.put(key, entry.getValue());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                            "잘못된 teamCulture입니다." + entry.getKey()
                    );
                }
            }
            postInfo.updateTeamCulture(converted);
        }

        Post updatedPost=postRepository.save(post);
        return PostDetailResDto.builder()
                .postId(updatedPost.getPostId())
                .leaderId(updatedPost.getLeaderId().getUserId())
                .title((updatedPost.getTitle()))
                .build();

    }


    //4. deletePost 공고 삭제
    @Transactional
    public void deletePost(Long id){
        Long postId= id;
        postRepository.deleteByPostId(postId);
    }

    //5. getDetailPost 공고 상세페이지 + 공고명 조회
    @Transactional(readOnly = true)
    public PostPageResDto getPostDetail(Long postId){
        Post post=postRepository.findPostDetailWithRecruitRolesById(postId)
                .orElseThrow(()-> new NoSuchElementException(postId+ " 해당 공고가 존재하지 않습니다."));
        List<PostCurrentRole> currentRoles= postCurrentRoleRepository.findByPostIdWithCurrenRole(postId);
        return PostPageResDto.from(post, currentRoles);
    }


}
