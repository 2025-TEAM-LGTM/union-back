package com.union.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioUpdateReqDto {
    @Size(max=255)
    private String title;

    @Size(max=255)
    private String summary;

    @Schema(example = "101")
    private Integer domainId;

    @Schema(example = "401")
    private Integer roleId;

    private Integer headcount;

    private String externUrl;

    private String Stext;

    private String Ttext;

    private String Atext;

    private String Rtext;

    private String imageKey;
    private Long imageSize;
}
