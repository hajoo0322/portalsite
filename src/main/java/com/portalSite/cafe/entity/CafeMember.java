package com.portalSite.cafe.entity;

import com.portalSite.cafe.dto.CafeMemberRequest;
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

    @Column(name = "is_deleted",nullable = false)
    private boolean isDeleted;

    private CafeMember(Cafe cafe, Member member, String cafeGrade, String nickname) {
        this.cafe = cafe;
        this.member = member;
        this.cafeGrade = cafeGrade;
        this.nickname = nickname;
    }

    public static CafeMember of(Cafe cafe, Member member, String cafeGrade, String nickname) {
        return new CafeMember(cafe, member, cafeGrade, nickname);
    }

    public void update(CafeMemberRequest cafeMemberRequest) {
        this.nickname = cafeMemberRequest.nickname();
    }

    public void delete(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
