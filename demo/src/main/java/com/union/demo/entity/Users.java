package com.union.demo.entity;
import com.union.demo.enums.JwtRole;
import com.union.demo.enums.PersonalityKey;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.awt.*;
import java.util.Map;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Users extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    @Column(name="login_id",nullable = false, unique = true, length =50)
    private String loginId;

    @Column(nullable = false, length =255)
    private String password;

    @Column(nullable = false, length =50)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="main_role_id")
    private Role mainRoleId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb") //jsonb 타입으로 받기
    private Map<PersonalityKey, Integer> personality;

    @Enumerated(EnumType.STRING)
    @Column(name="jwt_role", nullable = false)
    private JwtRole jwtRole;

    public String getHasRole(){
        return "ROLE_"+jwtRole.name();
    }


}
