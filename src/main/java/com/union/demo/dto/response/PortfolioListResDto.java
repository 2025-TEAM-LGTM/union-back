package com.union.demo.dto.response;

import com.union.demo.entity.Domain;
import com.union.demo.entity.Image;
import com.union.demo.entity.Portfolio;
import com.union.demo.entity.Role;
import com.union.demo.utill.S3UrlResolver;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PortfolioListResDto {
    private List<PortfoliosDto> portfolios;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PortfoliosDto{
        private Long portfolioId;
        private String title;
        private String summary;
        private ItemDto domain;
        private ItemDto role;
        private Integer headcount;
        private String imageUrl;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ItemDto{
        private Integer id;
        private String name;
    }

    //목록 전체 매핑
    public static PortfolioListResDto from(List<Portfolio> portfolios, S3UrlResolver s3UrlResolver){
        return PortfolioListResDto.builder()
                .portfolios(portfolios.stream()
                        .map(p-> fromPortfolio(p,s3UrlResolver))
                        .toList())
                .build();
    }

    //포폴 매핑
    private static PortfoliosDto fromPortfolio(Portfolio p,S3UrlResolver s3UrlResolver){

        Domain d=p.getDomain();
        Role r= p.getRole();
        Image i=p.getImage();

        return PortfoliosDto.builder()
                .portfolioId(p.getPortfolioId())
                .title(p.getTitle())
                .summary(p.getSummary())
                .domain(d!=null ? ItemDto.builder()
                        .id(d.getDomainId())
                        .name(d.getDomainName())
                        .build():null)
                .role(r!=null ? ItemDto.builder()
                        .id(r.getRoleId())
                        .name(r.getRoleName())
                        .build():null)
                .headcount(p.getHeadcount())
                .imageUrl(i!=null ? s3UrlResolver.toUrl(i.getS3Key()):null)
                .build();
    }

}
