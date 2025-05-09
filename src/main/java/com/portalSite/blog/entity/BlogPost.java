package com.portalSite.blog.entity;


import com.portalSite.common.BaseEntity;
import com.portalSite.member.entity.Member;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "blog_post", indexes = {
        @Index(name = "idx_blog_created_at", columnList = "created_at")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_board_id")
    private BlogBoard blogBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogHashtag> hashtagList = new ArrayList<>();


    public void update(BlogBoard blogBoard, String title, String description) {
        this.blogBoard = blogBoard == null ? this.blogBoard : blogBoard;
        this.title = title == null ? this.title : title;
        this.description = description == null ? this.description : description;
        hashtagList.clear();
    }

    public void addHashtag(Hashtag hashtag) {
        BlogHashtag postHashtag = BlogHashtag.of(this, hashtag);
        hashtagList.add(postHashtag);
    }

    private BlogPost(Blog blog, BlogBoard blogBoard, Member blogMember, String title, String description) {
        this.blog = blog;
        this.blogBoard = blogBoard;
        this.member = blogMember;
        this.title = title;
        this.description = description;
    }

    public static BlogPost of(Blog blog, BlogBoard blogBoard, Member blogMember, String title, String description) {
        return new BlogPost(blog, blogBoard, blogMember, title, description);
    }
}
