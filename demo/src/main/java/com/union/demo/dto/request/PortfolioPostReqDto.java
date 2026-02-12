package com.union.demo.dto.request;

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

    @NotBlank
    private Integer domainId;

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

    private String imageUrl;
}
