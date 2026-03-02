package com.union.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "refresh_token",
        indexes = {
                @Index(name = "ix_refresh_token_user_id", columnList = "user_id"),
                @Index(name = "ix_refresh_token_expires_at", columnList = "expires_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_refresh_token_token", columnNames = {"token"})
        }
)
public class RefreshToken extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="refresh_token_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id", nullable = false)
    private Users user;

    @Column(nullable = false, length = 512)
    private String token;

    @Column(name="expires_at", nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked=false;


    public static RefreshToken of(Users user, String token, Instant expiresAt){
        RefreshToken rt=new RefreshToken();
        rt.user=user;
        rt.token=token;
        rt.expiresAt=expiresAt;
        rt.revoked= false;
        return rt;
    }

    //token 폐기
    public void revoke(){
        this.revoked=true;
    }

    public boolean isExpired(Instant now){
        return expiresAt.isBefore(now);
    }


}
