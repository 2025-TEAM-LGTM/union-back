package com.union.demo.dto.response;

import com.union.demo.entity.Role;
import com.union.demo.entity.Users;
import com.union.demo.enums.PersonalityKey;
import com.union.demo.utill.S3UrlResolver;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class MemberMatchResDto {
    //프론트에 보낼 res dto

    private List<MemberMatchDto> members;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access=AccessLevel.PROTECTED)
    public static class MemberMatchDto{
        private Long userId;
        private String username;
        private String profileImageUrl;
        private ItemDto role;
        private List<ItemDto> hardSkill;
        private Map<PersonalityKey, Integer> personality;
        private String mainStrength;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access=AccessLevel.PROTECTED)
    public static class ItemDto{
        private Integer id;
        private String name;
    }

    public static MemberMatchDto from(Users user, List<ItemDto> skills, String mainStrength
    , S3UrlResolver s3UrlResolver){
        Role r=user.getMainRoleId();

        return MemberMatchDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .profileImageUrl(user.getImage()!=null?
                        s3UrlResolver.toUrl(user.getImage().getS3Key()):null)
                .role(ItemDto.builder()
                        .id(r.getRoleId())
                        .name(r.getRoleName())
                        .build())
                .hardSkill(skills != null ? skills : List.of())
                .personality(user.getPersonality())
                .mainStrength(mainStrength)
                .build();
    }
}
