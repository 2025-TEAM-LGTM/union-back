package com.union.demo.entity;

import com.union.demo.enums.RecruitStatus;
import com.union.demo.enums.TeamCultureKey;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Entity
@Getter
@Table(name="post_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pinfo_id")
    private Long pinfoId;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="post_id",
               nullable = false,
            foreignKey = @ForeignKey(name="post_info_post_id_fkey")
    )
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RecruitStatus status;

    @Column(name = "recruit_sdate")
    private OffsetDateTime recruitSdate;

    @Column(name = "recruit_edate")
    private OffsetDateTime recruitEdate;

    @Column(name = "contact", length = 255)
    private String contact;

    @Column(name = "about_us", columnDefinition = "text")
    private String aboutUs;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "team_culture", columnDefinition = "jsonb")
    private Map<TeamCultureKey,Integer> teamCulture;

    @Column(name = "seeking", columnDefinition = "text")
    private String seeking;

    @Column(name = "homepage_url", columnDefinition = "text")
    private String homepageUrl;

    // image_id -> image.image_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "image_id",
            foreignKey = @ForeignKey(name = "post_info_image_id_fkey")
    )
    private Image image;

}
