package com.union.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "[101, 103]")
    @NotEmpty
    List<@NotNull Integer> domainIds;

    @Schema(example = """
            {"startDate": "2026-01-15", 
            "endDate": "2026-02-10" }
            """)
    @NotNull
    private RecruitPeriodDto recruitPeriod;

    private String homepageUrl;

    @NotNull
    @Size(max=255)
    private String contact;

    @Schema(example = """
            [
                { "roleId": 301, "count": 1 },
                { "roleId": 404, "count": 1 }
             ]
            """)
    @NotNull
    private List<@Valid RoleCountDto> currentRoles;

    @Schema(example = """
            [
                { "roleId": 301, "count": 1 },
                { "roleId": 404, "count": 1 }
             ]
            """)
    @NotNull
    private List<@Valid RoleCountDto> recruitRoles;

    @NotNull
    private String seeking;

    @NotNull
    private String aboutUs;

    @Schema(example = """
            {
            "A": 1,
            "B": 1,
            "F": 0,
            "I": 0,
            "L": 0}
            """)
    @NotNull
    private Map<String, Integer> teamCulture;

    private String imageKey;
    private Long imageSize;

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
