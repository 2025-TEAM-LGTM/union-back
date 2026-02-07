package com.union.demo.dto.response;

import com.union.demo.enums.TeamCultureKey;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostCreateResDto {
    private Long postId;
    private Long leaderId;
    private String title;
    private List<Integer> domainIds;
    private RecruitPeriodDto recruitPeriod;
    private String contact;
    private String homepageUrl;
    private List<RoleCountDto> currentTeam;
    private List<RoleCountDto> recruitRoles;
    private String seeking;
    private String aboutUs;
    private Map<TeamCultureKey, Integer> teamCulture;
    private String imageUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecruitPeriodDto {
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RoleCountDto {
        private Integer roleId;
        private Integer count;
    }

}
