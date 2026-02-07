package com.union.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="leader_id",
            foreignKey = @ForeignKey(name = "post_leader_id_fkey")
    )
    private Users leaderId;

    @Column(length = 200)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="prime_domain_id",
            foreignKey = @ForeignKey(name = "post_prime_domain_id_fkey"
            ))
    private Domain primeDomainId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="second_domain_id",
            foreignKey = @ForeignKey(name = "post_second_domain_id_fkey"))
    private Domain secondDomainId;

    // post_info.post_id -> post.post_id
    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private PostInfo postInfo;

    // post_recruit_role.post_id -> post.post_id
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostRecruitRole> recruitRoles = new ArrayList<>();


}
