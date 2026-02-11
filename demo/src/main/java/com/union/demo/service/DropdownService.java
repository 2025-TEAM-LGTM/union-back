package com.union.demo.service;

import com.union.demo.dto.response.DropDownItemResDto;
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
    public List<DropDownItemResDto> dropdownRole(Long fieldId){
        List<Role> roles;

        roles = roleRepository.findByFieldId(fieldId);

        return roles.stream()
                // id가 long과 integer가 섞임 - upcasting으로 해결
                .map(r->new DropDownItemResDto(Long.valueOf(r.getRoleId()),r.getRoleName()))
                .toList();
    }
    //3. dropdownSkill
    private final SkillRepository skillRepository;

    public List<DropDownItemResDto> dropdownSkill(Integer fieldId){
        List<Skill> skills;
        skills = skillRepository.findByField_FieldId(fieldId);

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
}
