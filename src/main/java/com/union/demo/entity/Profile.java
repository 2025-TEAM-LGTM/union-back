package com.union.demo.entity;

import com.union.demo.enums.Gender;
import com.union.demo.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @Column(nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="university_id")
    private University university;

    private String major;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name="status",columnDefinition = "status_enum")
    private Status status;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name="gender",columnDefinition = "gender_enum")
    private Gender gender;

    @Column(name="entrance_year")
    private Integer entranceYear;

    @Column(name="birth_year")
    private Integer birthYear;

    public void updateEmail(String email){this.email=email;}
    public void updateEntranceYear(Integer entranceYear){this.entranceYear=entranceYear;}
    public void updateStatus(Status status){this.status=status;}
    public void updateUniversity(University university){
        this.university=university;
    }

}
