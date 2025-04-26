package com.portalSite.blog.entity;

import com.portalSite.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "blog_hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogHashtag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_post_id")
    private BlogPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    private BlogHashtag(BlogPost post, Hashtag hashtag) {
        this.post = post;
        this.hashtag = hashtag;
    }

    public static BlogHashtag of(BlogPost post, Hashtag hashtag) {
        return new BlogHashtag(post, hashtag);
    }

}
