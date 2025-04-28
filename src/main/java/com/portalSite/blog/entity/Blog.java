package com.portalSite.blog.entity;

import com.portalSite.common.BaseEntity;
import com.portalSite.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "blog")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "blog_name")
    private String blogName;

    @Column(columnDefinition = "TEXT")
    private String description;


    private Blog(Member member, String blogName, String description) {
        this.member = member;
        this.blogName = blogName;
        this.description = description;
    }

    public static Blog of(Member member, String blogName, String description) {
        return new Blog(member, blogName, description);
    }

    public void update(String blogName, String description) {
        this.blogName = blogName == null ? this.blogName : blogName;
        this.description = description == null ? this.description : description;
    }

}
