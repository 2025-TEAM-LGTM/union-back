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
    private Users user;

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


    public void updateTitle(String title){
        this.title=title;
    }

    public void updateHeadcount(Integer headcount){
        this.headcount=headcount;
    }

    public void updateExternUrl(String externUrl){
        this.externUrl=externUrl;
    }

    public void updateSummary(String summary){
        this.summary=summary;
    }

    public void updateStext(String Stext){
        this.Stext=Stext;
    }

    public void updateTtext(String Ttext){
        this.Ttext=Ttext;
    }

    public void updateAtext(String Atext){
        this.Atext=Atext;
    }

    public void updateRtext(String Rtext){
        this.Rtext=Rtext;
    }

    public void updateImage(Image image){
        this.image=image;
    }

    public void updateImageUrl(String imageUrl){

    }

    public void updateDomain(Domain domain){
        this.domain=domain;
    }

    public void updateRole(Role role){
        this.role=role;
    }


}
