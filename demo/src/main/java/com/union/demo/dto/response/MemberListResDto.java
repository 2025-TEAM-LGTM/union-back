package com.union.demo.dto.response;

import com.union.demo.entity.Role;
import com.union.demo.entity.Users;
import com.union.demo.enums.PersonalityKey;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter@Builder
@AllArgsConstructor @NoArgsConstructor(access= AccessLevel.PROTECTED)
public class MemberListResDto {
    private List<MemberDto> members;

    @Getter@Builder
    @AllArgsConstructor @NoArgsConstructor(access= AccessLevel.PROTECTED)
    public static class MemberDto{

        private Long userId;
        private String username;
        private String profileImageUrl;
        private ItemDto role;
        private List<ItemDto> hardSkill;
        private Map<PersonalityKey, Integer> personality;
    }

    @Getter@Builder
    @AllArgsConstructor @NoArgsConstructor(access= AccessLevel.PROTECTED)
    public static class ItemDto{
        private Integer id;
        private String name;
    }


    //mapping
    public static MemberDto from(
            Users user,
            List<ItemDto> skills
    ){
        Role r=user.getMainRoleId();

        return MemberDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .profileImageUrl(user.getImage()!=null?user.getImage().getImageUrl():null)
                .role(ItemDto.builder()
                        .id(r.getRoleId())
                        .name(r.getRoleName())
                        .build())
                .hardSkill(user.getUserSkills().stream()
                        .map(skill -> ItemDto.builder()
                                .id(skill.getSkill().getSkillId())
                                .name(skill.getSkill().getSkillName())
                                .build()
                        ).toList())
                .personality(user.getPersonality())
                .build();

    }



}
