package com.union.demo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@Entity
@Getter
@Table(name="post_apply",
    uniqueConstraints = {
        @UniqueConstraint(
                name="uk_post_user",
                columnNames = {"post_id","user_id"}
        )
    }

)
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="apply_id")
    private Long applyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private Users user;

    @Column(name="apply_date")
    private OffsetDateTime applyDate;

}
