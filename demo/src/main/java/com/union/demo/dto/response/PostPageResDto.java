package com.union.demo.dto.response;
import com.union.demo.entity.Image;
import com.union.demo.entity.Post;
import com.union.demo.entity.PostCurrentRole;
import com.union.demo.entity.PostInfo;
import com.union.demo.enums.TeamCultureKey;
import com.union.demo.utill.S3UrlResolver;
import lombok.*;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPageResDto {
    private Long postId;
    private LeaderDto leader;
    private String title;
    private Integer dday;

    private List<Integer> domainIds;
    private RecruitPeriodDto recruitPeriod;

    private String homepageUrl;
    private String contact;
    private List<RoleCountDto> currentRoles;
    private List<RoleCountDto> recruitRoles;
    private String seeking;
    private String aboutUs;
    private Map<TeamCultureKey, Integer> teamCulture;
    private String imageUrl;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LeaderDto{
        private Long userId;
        private String username;
        private String userImageUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecruitPeriodDto{
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RoleCountDto{
        private Integer roleId;
        private String roleName;
        private Integer count;
    }

    public static PostPageResDto from(Post post,
                                      Integer dday,
                                      List<PostCurrentRole> postCurrentRoles,
                                      S3UrlResolver s3UrlResolver){

        return PostPageResDto.builder()
                .postId(post.getPostId())
                .leader(
                        LeaderDto.builder()
                                .userId(post.getLeaderId().getUserId())
                                .username(post.getLeaderId().getUsername())
                                .userImageUrl(post.getLeaderId().getImage() !=null ?
                                        s3UrlResolver.toUrl(post.getLeaderId().getImage().getS3Key()):null)
                                .build()
                )
                .title(post.getTitle())
                .dday(dday)
                .domainIds(
                        List.of(
                                post.getPrimeDomainId().getDomainId(),
                                post.getSecondDomainId().getDomainId()
                        )
                )
                .recruitPeriod(
                        RecruitPeriodDto.builder()
                                .startDate(post.getPostInfo().getRecruitSdate().toLocalDate())
                                .endDate((post.getPostInfo().getRecruitEdate().toLocalDate()))
                                .build()
                )
                .homepageUrl(post.getPostInfo().getHomepageUrl())
                .contact(post.getPostInfo().getContact())
                .currentRoles(
                        postCurrentRoles.stream()
                                .map(cr -> RoleCountDto.builder()
                                        .roleId(cr.getRole().getRoleId())
                                        .roleName(cr.getRole().getRoleName())
                                        .count(cr.getCount())
                                        .build()
                                ).toList()
                )
                .recruitRoles(
                        post.getRecruitRoles().stream()
                                .map(rr -> RoleCountDto.builder()
                                        .roleId(rr.getRole().getRoleId())
                                        .roleName(rr.getRole().getRoleName())
                                        .count(rr.getCount())
                                        .build()
                                )
                                .toList()
                )
                .seeking(post.getPostInfo().getSeeking())
                .aboutUs(post.getPostInfo().getAboutUs())
                .teamCulture(post.getPostInfo().getTeamCulture())
                .imageUrl(
                        Optional.ofNullable(post.getPostInfo())
                                .map(PostInfo::getImage)
                                .map(Image::getS3Key)
                                .map(s3UrlResolver::toUrl)
                                .orElse(null)
                )
                .build();
    }
}
