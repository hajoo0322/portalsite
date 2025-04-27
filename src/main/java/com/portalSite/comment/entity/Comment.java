package com.portalSite.comment.entity;

import com.portalSite.blog.entity.Blog;
import com.portalSite.cafe.entity.Cafe;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.common.BaseEntity;
import com.portalSite.member.entity.Member;
import com.portalSite.news.entity.News;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private CafePost cafePost;

    @Setter
    @Column(nullable = false)
    private String content;

    private Comment(Member member, Blog blog, News news, CafePost cafePost, String content) {
        this.member = member;
        this.blog = blog;
        this.news = news;
        this.cafePost = cafePost;
        this.content = content;
    }

    public static Comment of(Member member, Blog blog, News news, CafePost cafePost, String content) {
        return new Comment(member, blog, news, cafePost, content);
    }
}
