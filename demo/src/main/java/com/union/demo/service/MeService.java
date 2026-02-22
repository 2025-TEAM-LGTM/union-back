package com.union.demo.service;

import com.union.demo.dto.request.PortfolioPostReqDto;
import com.union.demo.dto.request.PortfolioUpdateReqDto;
import com.union.demo.dto.request.ProfileUpdateReqDto;
import com.union.demo.dto.response.ProfileResDto;
import com.union.demo.dto.response.PortfolioDetailResDto;
import com.union.demo.dto.response.PortfolioListResDto;
import com.union.demo.entity.*;
import com.union.demo.enums.PersonalityKey;
import com.union.demo.enums.Purpose;
import com.union.demo.enums.Status;
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
public class MeService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final ImageRepository imageRepository;
    private final UserSkillRepository userSkillRepository;
    private final SkillRepository skillRepository;
    private final PortfolioRepository portfolioRepository;
    private final DomainRepository domainRepository;
    private final RoleRepository roleRepository;
    private final S3UrlResolver s3UrlResolver;


    //1. getProfile 프로필 조회
    public ProfileResDto getMyProfile(Long userId){
        //유저 존재여부 확인
        Users user =userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 userId입니다."));

        //user가 profile을 가지고 있는지 확인
        Profile profile  =profileRepository.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 profile입니다."));


        //return
        return ProfileResDto.from(
                user, profile, s3UrlResolver
        );

    }

    //2. updateProfile 프로필 수정
    @Transactional
    public ProfileResDto updateMyProfile(Long userId, ProfileUpdateReqDto req){

        //유저 존재여부 확인
        Users user =userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 userId입니다."));
        Long nowUserId=user.getUserId();

        //user가 profile을 가지고 있는지 확인
        Profile profile  =profileRepository.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 profile입니다."));

        //로그인한 유저와 profile의 주인인 유저가 서로 같은지 확인
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


        //image update
        if(req.getImageKey()!=null){
            Image oldImage=profile.getUser().getImage();
            String newKey=req.getImageKey().isBlank()?null:req.getImageKey();

            // new image
            if (newKey == null) {
                user.updateImage(null);
            }
            else {
                if(oldImage==null){
                    Image newImage = imageRepository.save(
                            Image.builder()
                                    .s3Key(req.getImageKey())
                                    .fileSize(req.getImageSize())
                                    .purpose(Purpose.PROFILE)
                                    .build()
                    );
                    user.updateImage(newImage);
                }
                else{
                    oldImage.updateS3Key(req.getImageKey());
                    oldImage.updateFileSize(req.getImageSize());
                }

            }

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

        return ProfileResDto.from(updatedUser,updatedProfile,s3UrlResolver);
    }

    //3. getPortfolios 포폴 목록 조회
    public PortfolioListResDto getPortfolioList(Long userId){
        userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자 입니다."+userId));

        List<Portfolio> portfolios= portfolioRepository.findPortfolioByUserId(userId);

        return PortfolioListResDto.from(portfolios,s3UrlResolver);

    }

    //4. postPortfolio 포폴 업로드
    @Transactional
    public PortfolioDetailResDto createPortfolio(Long userId, PortfolioPostReqDto req){
        //user 확인
        Users user=userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자 입니다."+userId));

        //domain, role id 유효성 확인
        Domain domain=domainRepository.findByDomainId(req.getDomainId())
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 domainId 입니다."+req.getDomainId()));
        Role role=roleRepository.findByRoleId(req.getRoleId())
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 roleId 입니다."+req.getRoleId()));

        //role로 field 찾기
        Field field=roleRepository.findFieldByRoleId(role.getRoleId())
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 field 입니다."));


        //image 저장
        //image api 처리 이후 수정 필요
        Image image=null;
        if(req.getImageKey()!=null && !req.getImageKey().isBlank()){
            image=imageRepository.save(
                    Image.builder()
                            .s3Key(req.getImageKey())
                            .purpose(Purpose.PORTFOLIO)
                            .fileSize(req.getImageSize())
                            .build()
            );
        }

        //portfolio 생성 및 저장
        Portfolio newPortfolio= Portfolio.builder()
                .user(user)
                .title(req.getTitle())
                .summary(req.getSummary())
                .domain(domain)
                .role(role)
                .headcount(req.getHeadcount())
                .externUrl(req.getExternUrl())
                .Stext(req.getStext())
                .Ttext(req.getTtext())
                .Atext(req.getAtext())
                .Rtext(req.getRtext())
                .image(image)
                .build();

        Portfolio saved=portfolioRepository.save(newPortfolio);

        return PortfolioDetailResDto.from(saved,s3UrlResolver);

    }


    //5. updatePortfolio 포폴 수정
    @Transactional
    public PortfolioDetailResDto updatePortfolio(Long userId, Long portfolioId, PortfolioUpdateReqDto req){
        //유저 확인
        Users user=userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 유저입니다."));

        //포트폴리오 확인
        Portfolio portfolio=portfolioRepository.findPortfolioByPortfolioId(portfolioId)
                .orElseThrow(()-> new NoSuchElementException("존재하지 않는 포트폴리오 입니다."));

        //권한 확인: 로그인된 유저 == portfolio 작성자
        if(!portfolio.getUser().getUserId().equals(user.getUserId())){
            throw new IllegalArgumentException("본인 포트폴리오만 수정할 수 있습니다.");
        }

        //변경
        if (req.getTitle() != null) {
            portfolio.updateTitle(req.getTitle());
        }
        if (req.getSummary() != null) {
            portfolio.updateSummary(req.getSummary());
        }
        if (req.getHeadcount() != null) {
            portfolio.updateHeadcount(req.getHeadcount());
        }
        if (req.getExternUrl() != null) {
            portfolio.updateExternUrl(req.getExternUrl());
        }
        if (req.getStext() != null) {
            portfolio.updateStext(req.getStext());
        }
        if (req.getTtext() != null) {
            portfolio.updateTtext(req.getTtext());
        }
        if (req.getAtext() != null) {
            portfolio.updateAtext(req.getAtext());
        }
        if (req.getRtext() != null) {
            portfolio.updateRtext(req.getRtext());
        }

        // domain 변경
        if (req.getDomainId() != null) {
            Domain domain = domainRepository.findByDomainId(req.getDomainId())
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 domainId 입니다."));
            portfolio.updateDomain(domain);
        }

        // role 변경
        if (req.getRoleId() != null) {
            Role role = roleRepository.findByRoleId(req.getRoleId())
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 roleId 입니다."));
            portfolio.updateRole(role);
        }

        // image
        if (req.getImageKey() != null) {
            String newKey=req.getImageKey().isBlank()?null:req.getImageKey();

            Image oldImage=portfolio.getImage();

            if(newKey==null){
                portfolio.updateImage(null);
            }
            else{
                if(oldImage==null){
                    Image newImage=imageRepository.save(
                            Image.builder()
                                    .s3Key(newKey)
                                    .fileSize(req.getImageSize())
                                    .purpose(Purpose.PORTFOLIO)
                                    .build()
                    );
                    portfolio.updateImage(newImage);
                }else{
                    oldImage.updateS3Key(newKey);
                    oldImage.updateFileSize(req.getImageSize());
                }
            }

        }

        Portfolio updatedPortfolio=portfolioRepository.findPortfolioByPortfolioId(portfolioId)
                .orElseThrow(()-> new NoSuchElementException("존재하지 않는 portfolio입니다."));

        return PortfolioDetailResDto.from(updatedPortfolio,s3UrlResolver);
    }


    //6. deletePortfolio 포폴 삭제
    @Transactional
    public void deletePortfolio(Long userId, Long portfolioId){
        //user 존재 확인
        Users nowUser=userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Long nowUserId=nowUser.getUserId();

        //포폴 존재 확인
        Portfolio portfolio=portfolioRepository.findDetailByPortfolioId(portfolioId)
                .orElseThrow(()-> new NoSuchElementException("존재하지 않는 portfolioId입니다."));

        //포폴 주인과 로그인한 사람이 같은지 확인
        if(!nowUserId.equals(portfolio.getUser().getUserId())){
            throw new AccessDeniedException("포폴 작성자와 로그인한 사람이 다릅니다. 권한 없습니다.");
        }

        portfolioRepository.deletePortfolioByPortfolioId(portfolio.getPortfolioId());

    }

    //7. getDetailPortfolio 세부 포폴 페이지 조회
    public PortfolioDetailResDto getPortfolioDetail(Long userId, Long portfolioId){
        userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Portfolio portfolio=portfolioRepository.findDetailByPortfolioId(portfolioId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 portfolioId입니다."));

        return PortfolioDetailResDto.from(portfolio,s3UrlResolver);
    }
}
