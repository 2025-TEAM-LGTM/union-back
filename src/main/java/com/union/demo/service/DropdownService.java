package com.union.demo.service;

import com.union.demo.dto.response.DropDownItemResDto;
import com.union.demo.dto.response.DropDownPersonalityResDto;
import com.union.demo.dto.response.DropdownRoleResDto;
import com.union.demo.entity.Domain;
import com.union.demo.entity.Role;
import com.union.demo.entity.Skill;
import com.union.demo.entity.University;
import com.union.demo.repository.DomainRepository;
import com.union.demo.repository.RoleRepository;
import com.union.demo.repository.SkillRepository;
import com.union.demo.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DropdownService {
    //1. dropdownUniversity
    private final UniversityRepository universityRepository;

    public List<DropDownItemResDto> dropdownUniversity(String q){
        List<University> universities;

        if(!StringUtils.hasText(q)){ // q가 null이면 전체 목록 보여줌
            universities = universityRepository.findAll();
        }
        else{
            universities = universityRepository.findByUnivNameContaining(q.trim());
        }

        return universities.stream()
                .map(u -> new DropDownItemResDto(u.getUnivId(), u.getUnivName()))
                .toList();
    }

    //2. dropdownRole
    private final RoleRepository roleRepository;
    public List<DropdownRoleResDto> dropdownRole(Long fieldId){
        List<Role> roles;

        if(fieldId==null){ //param이 없으면 전체 role 반환
            roles=roleRepository.findAll();
        }
        else{
            roles= roleRepository.findByField_FieldId(fieldId);
        }

        return  roles.stream()
                .map(r -> new DropdownRoleResDto(
                        Long.valueOf(r.getRoleId()),
                        r.getRoleName(),
                        Long.valueOf(r.getField().getFieldId()),
                        r.getField().getFieldName()
                )).toList();
    }


    //3. dropdownSkill
    private final SkillRepository skillRepository;

    public List<DropDownItemResDto> dropdownSkill(Integer fieldId){
        List<Skill> skills;

        if(fieldId==null){ //param이 없으면 전체 skills 반환
            skills=skillRepository.findAll();
        }
        else {
            skills = skillRepository.findByField_FieldId(fieldId);
        }

        return skills.stream()
                .map(s->new DropDownItemResDto(Long.valueOf(s.getSkillId()), s.getSkillName()))
                .toList();

    }
    //4. dropdownDomain
    private final DomainRepository domainRepository;
    public List<DropDownItemResDto> dropdownDomain(){
        List<Domain> domains;
        domains = domainRepository.findAll();

        return domains.stream()
                .map(d -> new DropDownItemResDto(d.getDomainId().longValue(), d.getDomainName())).toList();
    }

    //5. dropdown Personality
    public DropDownPersonalityResDto dropdownPersonality() {
        return DropDownPersonalityResDto.builder()
                .items(List.of(
                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("A")
                                .label("의사결정 구조")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("수평적 논의형")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("리더 중심형")
                                        .build())
                                .build(),

                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("B")
                                .label("업무 진행 방식")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("체계적, 계획형")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("유연한, 즉흥형")
                                        .build())
                                .build(),

                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("C")
                                .label("피드백 문화")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("직접적 즉각적 피드백 선호")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("신중하고 완곡한 피드백 선호")
                                        .build())
                                .build(),

                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("D")
                                .label("업무 자율성")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("자율적 환경 선호")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("명확한 지시 및 역할 분담 선호")
                                        .build())
                                .build(),

                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("E")
                                .label("성과 평가 기준")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("결과 중심 (성과, 결과물)")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("과정 중심 (성장, 배움)")
                                        .build())
                                .build(),

                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("F")
                                .label("커뮤니케이션 분위기")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("공식적 / 문서 중심")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("자유로운 / 캐주얼한")
                                        .build())
                                .build(),

                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("G")
                                .label("팀 분위기")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("진지하고 목표 지향적")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("즐겁고 유대감 중심")
                                        .build())
                                .build(),

                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("H")
                                .label("갈등 해결 방식")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("즉시 논의하여 해결")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("일정 시간 두고 정리 후 대화")
                                        .build())
                                .build(),

                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("I")
                                .label("일과 생활의 균형")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("워라밸 중시")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("성취 및 몰입 중시")
                                        .build())
                                .build(),

                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("J")
                                .label("변화에 대한 태도")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("새로운 시도와 변화에 적극적")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("안정성과 일관성을 선호")
                                        .build())
                                .build(),

                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("K")
                                .label("협업 스타일")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("리더형")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("서포터형")
                                        .build())
                                .build(),

                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("L")
                                .label("팀 내 역할 분담 선호")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("역할 고정형 (분업 중심)")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("역할 유연형 (상황에 따라 교차 참여)")
                                        .build())
                                .build(),

                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("M")
                                .label("성과 공유 방식")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("팀 전체 성과 중심")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("개인 기여 중심")
                                        .build())
                                .build(),

                        DropDownPersonalityResDto.ItemDto.builder()
                                .key("N")
                                .label("회의 문화")
                                .first(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(0)
                                        .name("자주 짧게, 빠른 피드백")
                                        .build())
                                .second(DropDownPersonalityResDto.OptionDto.builder()
                                        .code(1)
                                        .name("드물게 깊게, 구조적 논의")
                                        .build())
                                .build()
                ))
                .build();
    }


}
