package com.union.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Table(name="portfolio")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Portfolio extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="portfolio_id", nullable = false)
    private Long portfolioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private Users userId;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="field_id")
    private Field field;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="domain_id")
    private Domain domain;

    private Integer headcount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="image_id")
    private Image image;

    @Column(name="extern_url")
    private String externUrl;

    private String summary;

    @Column(name="s_text", columnDefinition = "text")
    private String Stext;

    @Column(name="t_text", columnDefinition = "text")
    private String Ttext;

    @Column(name="a_text", columnDefinition = "text")
    private String Atext;

    @Column(name="r_text", columnDefinition = "text")
    private String Rtext;

}
