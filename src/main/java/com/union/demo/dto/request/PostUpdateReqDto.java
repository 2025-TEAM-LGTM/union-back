package com.union.demo.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter@Setter
@NoArgsConstructor
public class PostUpdateReqDto {
    String title;

    @Schema(example = "[101, 103]")
    List<Integer> domainIds;

    @Schema(example = """
            {"startDate": "2026-01-15", 
            "endDate": "2026-02-10" }
            """)
    private RecruitPeriodDto recruitPeriod;

    private String homepageUrl;

    private String contact;

    @Schema(example = """
            [
                { "roleId": 301, "count": 1 },
                { "roleId": 404, "count": 1 }
             ]
            """)
    private List<RoleCountDto> currentRoles;

    @Schema(example = """
            [
                { "roleId": 301, "count": 1 },
                { "roleId": 404, "count": 1 }
             ]
            """)
    private List<RoleCountDto> recruitRoles;

    private String seeking;
    private String aboutUs;

    @Schema(example = """
            [
                { "roleId": 301, "count": 1 },
                { "roleId": 404, "count": 1 }
             ]
            """)
    private Map<String, Integer> teamCulture;

    private String imageKey;
    private Long imageSize;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class RecruitPeriodDto{
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class RoleCountDto{
        private Integer roleId;
        private Integer count;
    }

}
