package com.union.demo.service;

import com.union.demo.dto.request.ProfileUpdateReqDto;
import com.union.demo.dto.response.MyProfileResDto;
import com.union.demo.entity.*;
import com.union.demo.enums.PersonalityKey;
import com.union.demo.enums.Status;
import com.union.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final ImageRepository imageRepository;
    private final UserSkillRepository userSkillRepository;
    private final SkillRepository skillRepository;


    //1. getProfile 프로필 조회
    public MyProfileResDto getMyProfile(Long userId){
        //유저 존재여부 확인
        Users user =userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 userId입니다."));

        //user가 profile을 가지고 있는지 확인
        Profile profile  =profileRepository.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 profile입니다."));


        //return
        return MyProfileResDto.from(
                user, profile
        );

    }

    //2. updateProfile 프로필 수정
    @Transactional
    public MyProfileResDto updateMyProfile(Long userId, ProfileUpdateReqDto req){

        //유저 존재여부 확인
        Users user =userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 userId입니다."));

        //user가 profile을 가지고 있는지 확인
        Profile profile  =profileRepository.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 profile입니다."));

        //로그인한 유저와 profile의 주인인 유저가 서로 같은지 확인
        //service에 오기 전 jwtFilter에서 먼저 체크가 될 것이지만 한번 더 체크
        Long nowUserId= (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if(!profile.getUserId().equals(nowUserId)){
            throw new AccessDeniedException("profile update 권한이 없습니다.");
        }

        //update
        if(req.getEmail()!=null) {
            profile.updateEmail(req.getEmail());
        }
        if(req.getEntranceYear()!=null) {
            profile.updateEntranceYear(req.getEntranceYear());
        }

        //status(String -> Enum)
        if(req.getStatus()!=null){
            try{
                Status status= Status.valueOf(req.getStatus().toUpperCase());
                profile.updateStatus(status);
            }catch (IllegalArgumentException e){
                throw new IllegalArgumentException("status 값이 올바르지 않습니다.");
            }
        }

        //university
        if(req.getUniversityId()!=null ){
            University univ=universityRepository.findByUnivId(req.getUniversityId())
                    .orElseThrow(()->new IllegalArgumentException("존재하지 않는 대학 id입니다."));
            profile.updateUniversity(univ);
        }

        //personality(string -> PersonalityKey)
        if(req.getPersonality()!=null){
            Map<PersonalityKey, Integer> converted=new EnumMap<>(PersonalityKey.class);

            for(var entry: req.getPersonality().entrySet()){
                try{
                    PersonalityKey key=PersonalityKey.valueOf(entry.getKey().toUpperCase());
                    Integer value=entry.getValue();//모든 personality에 입력을 다 해야함
                    converted.put(key, value);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("personality key, value가 올바르지 않습니다");
                }
            }
            user.updatePersonality(converted);
        }


        //image
        //image 부분은 s3 연결한 이후 수정할 부분 있으면 수정해야함!
        if(req.getProfileImageUrl()!=null){
            //새로운 이미지를 image 테이블에 추가
            Image newImage=imageRepository.save(Image.builder()
                            .imageUrl(req.getProfileImageUrl())
                    .build());

            Image oldImage=user.getImage();
            user.updateImage(newImage);

            //기존 이미지 행을 지워야 되는거면
            //기존 이미지를 지우는 코드 추가하기
            //Image oldImage=user.getImage();
            //imageRepository.delete(oldImage);
        }

        //hardSkill
        if(req.getHardSkills()!=null){
            //skill id 만 추출
            Set<Integer> reqSillIds=req.getHardSkills().stream()
                    .filter(Objects::nonNull)
                            .collect(Collectors.toSet());

            //기존 skill 들 삭제
            userSkillRepository.deleteByUser_UserId(user.getUserId());

            //새로운 skill들을 save
            if(!reqSillIds.isEmpty()){
                List<Skill> skills=skillRepository.findAllBySkillIdIn(reqSillIds);

                //요청한 id가 db에 없는 id이면 error
                if(skills.size()!=reqSillIds.size()){
                    Set<Integer> found=skills.stream().map(Skill::getSkillId).collect(Collectors.toSet());
                    reqSillIds.removeAll(found); //db에 있는 것들을 지움으로써 db에 없는 id만 남기기
                    throw new IllegalArgumentException("존재하지 않는 skillId입니다."+reqSillIds);

                }

                List<UserSkill> newUserSkill=skills.stream()
                        .map(skill -> UserSkill.builder()
                                .user(user)
                                .skill(skill)
                                .build())
                        .toList();

                userSkillRepository.saveAll(newUserSkill);

            }
        }

        Profile updatedProfile=profileRepository.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 profile입니다."));
        Users updatedUser=userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 user입니다."));
        return MyProfileResDto.from(updatedUser,updatedProfile);
    }

    //3. getPortfolios 포폴 목록 조회

    //4. postPortfolio 포폴 업로드

    //5. updatePortfolio 포폴 수정

    //6. deletePortfolio 포폴 삭제

    //7. getDetailPortfolio 세부 포폴 페이지 조회
}
