package com.union.demo.service;

import com.union.demo.dto.response.MemberListResDto;
import com.union.demo.entity.UserSkill;
import com.union.demo.entity.Users;
import com.union.demo.enums.PersonalityKey;
import com.union.demo.repository.MemberRepository;
import com.union.demo.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService{
    private final MemberRepository memberRepository;
    private final UserSkillRepository userSkillRepository;

    // getMembers 함수 + 필터링
    public MemberListResDto getMembers(
            List<Integer> roleIds,
            List<Integer> hardSkillIds,
            Map<PersonalityKey, Integer> personality
    ) {
        //필터링 조건에 맞는 유저들 모으기
        List<Users> users = memberRepository.searchMembers(roleIds, hardSkillIds, personality);

        if (users.isEmpty()) {
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
                .map(user-> MemberListResDto.from(user, skillsByUserId.getOrDefault(user.getUserId(),List.of())))
                .toList();

        return MemberListResDto.builder()
                .members(memberDtos)
                .build();
    }


    // getMemberProfile 함수
}
