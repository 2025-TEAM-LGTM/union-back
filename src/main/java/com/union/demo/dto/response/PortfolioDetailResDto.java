package com.union.demo.dto.response;

import com.union.demo.entity.Domain;
import com.union.demo.entity.Image;
import com.union.demo.entity.Portfolio;
import com.union.demo.entity.Role;
import com.union.demo.utill.S3UrlResolver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PortfolioDetailResDto {
    private Long portfolioId;
    private String title;
    private String summary;
    private ItemDto domain;
    private ItemDto role;
    private Integer headcount;
    private String externUrl;
    private String Stext;
    private String Ttext;
    private String Atext;
    private String Rtext;
    private String imageUrl;


    @Getter
    @Builder
    @AllArgsConstructor
    public static class ItemDto{
        private Integer id;
        private String name;
    }

    public static PortfolioDetailResDto from(Portfolio portfolio, S3UrlResolver s3UrlResolver){
        Domain d= portfolio.getDomain();
        Role r= portfolio.getRole();
        Image i=portfolio.getImage();

        return PortfolioDetailResDto.builder()
                .portfolioId(portfolio.getPortfolioId())
                .title(portfolio.getTitle())
                .summary(portfolio.getSummary())
                .domain( d!=null ? ItemDto.builder()
                        .id(d.getDomainId())
                        .name(d.getDomainName())
                        .build():null
                )
                .role(r!=null ? ItemDto.builder()
                        .id(r.getRoleId())
                        .name(r.getRoleName()).build():null)
                .headcount(portfolio.getHeadcount())
                .externUrl(portfolio.getExternUrl())
                .Stext(portfolio.getStext())
                .Ttext(portfolio.getTtext())
                .Atext(portfolio.getAtext())
                .Rtext(portfolio.getRtext())
                .imageUrl(i!=null?s3UrlResolver.toUrl(i.getS3Key()):null)
                .build();

    }
}
