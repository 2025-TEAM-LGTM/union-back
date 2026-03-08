package com.union.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name="post_recruit_role")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostRecruitRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="precruit_id")
    private Long precruitId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="post_id",
                nullable = false,
                foreignKey = @ForeignKey(name="post_recruit_role_post_id_fkey"))
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "role_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "post_recruit_role_role_id_fkey")
    )
    private Role role;

    @Column(name = "count", nullable = false)
    private Integer count;


}
