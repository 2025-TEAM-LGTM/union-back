package com.union.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name="post_current_role")
@Getter@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostCurrentRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pcurrent_id")
    private Long pcurrentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="post_id",
    nullable = false,foreignKey = @ForeignKey(name="post_current_role_post_id_fkey"))
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="role_id",
    nullable = false, foreignKey = @ForeignKey(name="post_current_role_role_id_fkey"))
    private Role role;

    @Column(name="count")
    private Integer count=1;

}
