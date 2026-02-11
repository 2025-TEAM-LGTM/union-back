package com.union.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="skill")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Skill {
    @Id
    @Column(name="skill_id")
    private Integer skillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="field_id",nullable = false)
    private Field field;

    @Column(name="skill_name", nullable = false,length = 50)
    private String skillName;

}
