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
public class ProfileResDto {
    private String username;
    private String profileImageUrl;
    private Integer birthYear;
    private ItemDto mainRole;
    private String email;
    private UnivResDto university;
    private Integer entranceYear;
    private String status;
    private List<ItemDto> hardSkills;
    private Map<PersonalityKey, Integer> personality;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ItemDto{
        private Integer id;
        private String name;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UnivResDto{
        private Long id;
        private String name;
    }

    //dto 만들기
    public static ProfileResDto from(
            Users user,
            Profile profile
    ){
        return ProfileResDto.builder()
                .username(user.getUsername())
                .profileImageUrl(user.getImage()!=null? user.getImage().getImageUrl():null)
                .birthYear(profile.getBirthYear())
                .mainRole(ItemDto.builder()
                        .id(user.getMainRoleId().getRoleId())
                        .name(user.getMainRoleId().getRoleName()  )
                        .build())
                .email(profile.getEmail())
                .university(UnivResDto.builder()
                        .id(profile.getUniversity().getUnivId())
                        .name(profile.getUniversity().getUnivName())
                        .build())
                .entranceYear(profile.getEntranceYear())
                .status(profile.getStatus().toString())
                .hardSkills(user.getUserSkills().stream()
                        .map(skill -> ItemDto.builder()
                                .id(skill.getSkill().getSkillId())
                                .name(skill.getSkill().getSkillName())
                                .build()
                        ).toList())
                .personality(user.getPersonality())
                .build();
    }
}
