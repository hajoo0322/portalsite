package com.portalSite.blog.entity;


import com.portalSite.common.BaseEntity;
import com.portalSite.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "blog_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "blog_board_id")
    private BlogBoard blogBoard;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "blog_post")
    private List<BlogHashtag> hashtagList;


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

    public void update(BlogBoard blogBoard, String title, String description) {
        this.blogBoard = blogBoard
        this.title = title;
        this.description = description;
    }
}
