package com.union.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioPostReqDto {
    @NotBlank
    @Size(max=255)
    private String title;

    @NotBlank
    @Size(max=255)
    private String summary;

    @Schema(example = "101")
    @NotBlank
    private Integer domainId;

    @Schema(example = "401")
    @NotBlank
    private Integer roleId;

    @NotBlank
    private Integer headcount;

    private String externUrl;

    @NotBlank
    private String Stext;
    @NotBlank
    private String Ttext;
    @NotBlank
    private String Atext;
    @NotBlank
    private String Rtext;

    private String imageKey;
    private Long imageSize;
}
