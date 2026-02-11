package com.union.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="university")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="univ_id")
    private Long univId;

    @Column(name="univ_name")
    private String univName;

}
