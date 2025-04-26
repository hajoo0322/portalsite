package com.portalSite.blog.entity;

import com.portalSite.blog.entity.Blog;
import com.portalSite.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "blog_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @Column(name = "board_name")
    private String category;


    private BlogBoard(Blog blog, String category) {
        this.blog = blog;
        this.category = category;
    }

    public static BlogBoard of(Blog blog, String category) {
        return new BlogBoard(blog, category);
    }

    public void update(String category) {
        this.category = category;
    }
}
