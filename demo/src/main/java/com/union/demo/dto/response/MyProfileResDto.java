package com.union.demo.dto.response;

import com.union.demo.entity.Profile;
import com.union.demo.entity.Users;
import com.union.demo.enums.PersonalityKey;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyProfileResDto {
    private String username;
    private String profileImageUrl;
    private Integer birthYear;
    private RoleResDto mainRole;
    private String email;
    private UnivResDto university;
    private Integer entranceYear;
    private String status;
    private List<HardSkillDto> hardSkills;
    private Map<PersonalityKey, Integer> personality;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RoleResDto{
        private Integer roleId;
        private String roleName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class HardSkillDto{
        private Integer skillId;
        private String skillName;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UnivResDto{
        private Long univId;
        private String univName;
    }

    //dto 만들기
    public static MyProfileResDto from(
            Users user,
            Profile profile
    ){
        return MyProfileResDto.builder()
                .username(user.getUsername())
                .profileImageUrl(user.getImage()!=null? user.getImage().getImageUrl():null)
                .birthYear(profile.getBirthYear())
                .mainRole(RoleResDto.builder()
                        .roleId(user.getMainRoleId().getRoleId())
                        .roleName(user.getMainRoleId().getRoleName()  )
                        .build())
                .email(profile.getEmail())
                .university(UnivResDto.builder()
                        .univId(profile.getUniversity().getUnivId())
                        .univName(profile.getUniversity().getUnivName())
                        .build())
                .entranceYear(profile.getEntranceYear())
                .status(profile.getStatus().toString())
                .hardSkills(user.getUserSkills().stream()
                        .map(skill -> HardSkillDto.builder()
                                .skillId(skill.getSkill().getSkillId())
                                .skillName(skill.getSkill().getSkillName())
                                .build()
                        ).toList())
                .personality(user.getPersonality())
                .build();
    }
}
