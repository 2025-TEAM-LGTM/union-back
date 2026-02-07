package com.union.demo.dto.request;

import com.union.demo.entity.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
public class PostCreateReqDto {
    @NotBlank
    @Size(max=200)
    String title;

    @NotEmpty
    List<@NotNull Integer> domainIds;

    @NotNull
    private RecruitPeriodDto recruitPeriod;

    @NotNull
    @Size(max=255)
    private String contact;

    private String homepageUrl;

    @NotNull
    private List<@Valid RoleCountDto> currentTeam;

    @NotNull
    private List<@Valid RoleCountDto> recruitRoles;

    @NotNull
    private String seeking;

    @NotNull
    private String aboutUs;

    @NotNull
    private Map<String, Integer> teamCulture;

    private String imageUrl;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class RecruitPeriodDto{
        @NotNull
        private LocalDate startDate;
        @NotNull
        private LocalDate endDate;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class RoleCountDto{
        @NotNull
        private Integer roleId;
        @NotNull
        private Integer count;
    }

}
