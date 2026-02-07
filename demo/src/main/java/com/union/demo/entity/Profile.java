package com.union.demo.entity;

import com.union.demo.enums.Gender;
import com.union.demo.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name="user_profile")
public class Profile {
    @Id
    private Long userId; //pk + fk

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name="user_id")
    private Users user;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="university_id")
    private University university;

    private String major;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name="entrance_year")
    private Integer entranceYear;

    @Column(name="birth_year")
    private Integer birthYear;


}
