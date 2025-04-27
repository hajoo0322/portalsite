package com.portalSite.comment.entity;

import com.portalSite.blog.entity.BlogPost;
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
    @JoinColumn(name = "blog_post_id")
    private BlogPost blogPost;

    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;

    @ManyToOne
    @JoinColumn(name = "cafe_post_id")
    private CafePost cafePost;

    @Setter
    @Column(nullable = false)
    private String content;

    public Comment(Member member, Object post, String content, PostType type) {
        this.member = member;
        this.content = content;

        switch (type) {
            case BLOG -> this.blogPost = (BlogPost) post;
            case NEWS -> this.news = (News) post;
            case CAFE -> this.cafePost = (CafePost) post;
            default -> throw new RuntimeException("");
        }
    }

    public static Comment of(Member member, Object post, String content, PostType type) {
        return new Comment(member, post, content, type);
    }
}

//    private Comment(Member member, BlogPost blogPost, String content) {
//        this.member = member;
//        this.blogPost = blogPost;
//        this.content = content;
//    }
//
//    public Comment(Member member, News news, String content) {
//        this.member = member;
//        this.news = news;
//        this.content = content;
//    }
//
//    public Comment(Member member, CafePost cafePost, String content) {
//        this.member = member;
//        this.cafePost = cafePost;
//        this.content = content;
//    }

//    public static Comment of(Member member, BlogPost blogPost, String content) {
//        return new Comment(member, blogPost, content);
//    }
//
//    public static Comment of(Member member, News news, String content) {
//        return new Comment(member, news, content);
//    }
//
//    public static Comment of(Member member, CafePost cafePost, String content) {
//        return new Comment(member, cafePost, content);
//    }
