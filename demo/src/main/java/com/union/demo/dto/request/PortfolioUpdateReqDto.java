package com.union.demo.dto.request;

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

    private Integer domainId;

    private Integer roleId;

    private Integer headcount;

    private String externUrl;

    private String Stext;

    private String Ttext;

    private String Atext;

    private String Rtext;

    private String imageUrl;
}
