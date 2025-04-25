package com.portalSite.cafe.entity;

import com.portalSite.common.BaseEntity;
import com.portalSite.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cafe_member")
@NoArgsConstructor
public class CafeMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cafe_id",nullable = false)
    private Cafe cafe;

    @ManyToOne
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @Column(name = "cafe_grade",length = 12)
    private String cafeGrade;

    @Column(name = "visit_count",nullable = false)
    private int visitCount;

    @Column(name = "nickname",length = 12)
    private String nickname;

    public CafeMember(Cafe cafe, Member member, String cafeGrade, int visitCount, String nickname) {
        this.cafe = cafe;
        this.member = member;
        this.cafeGrade = cafeGrade;
        this.visitCount = visitCount;
        this.nickname = nickname;
    }
}
