package com.portalSite.cafe.entity;

import com.portalSite.cafe.dto.CafeMemberRequest;
import com.portalSite.common.BaseEntity;
import com.portalSite.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cafe_member",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cafe_id","nickname"})
    })
@NoArgsConstructor
public class CafeMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id",nullable = false)
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_level",nullable = false)
    private CafeLevel cafeLevel;

    @Column(name = "visit_count",nullable = false)
    private int visitCount;

    @Column(name = "nickname",length = 12)
    private String nickname;

    @Column(name = "is_deleted",nullable = false)
    private boolean isDeleted;

    @Column(name = "post_count",nullable = false)
    private int postCount;

    @Column(name = "comment_count",nullable = false)
    private int commentCount;

    private CafeMember(Cafe cafe, Member member, CafeLevel cafeLevel, String nickname) {
        this.cafe = cafe;
        this.member = member;
        this.cafeLevel = cafeLevel;
        this.nickname = nickname;
        this.visitCount=0;
        this.commentCount=0;
        this.postCount=0;
    }

    public static CafeMember of(Cafe cafe, Member member, CafeLevel cafeLevel, String nickname) {
        return new CafeMember(cafe, member, cafeLevel, nickname);
    }

    public void update(CafeMemberRequest cafeMemberRequest) {
        this.nickname = cafeMemberRequest.nickname();
    }

    public void updateCafeGrade(CafeLevel cafeLevel) {
        this.cafeLevel = cafeLevel;
    }

    public void delete(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
