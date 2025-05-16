package com.portalSite.search.document;

import com.portalSite.blog.entity.BlogPost;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.comment.entity.PostType;
import com.portalSite.news.entity.News;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Getter
@Document(indexName = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostSearchDocument {

    @Id
    private String id;
    private String title;
    private String description;
    private String writer;
    private LocalDateTime createdAt;
    private PostType postType;

    public static PostSearchDocument from(News news) {
        String id = PostType.NEWS.name() + "-" + news.getId();
        return new PostSearchDocument(
                id,
                news.getNewsTitle(),
                news.getDescription(),
                news.getMember().getName(),
                news.getCreatedAt(),
                PostType.NEWS
        );
    }

    public static PostSearchDocument from(BlogPost blogPost) {
        String id = PostType.BLOG.name() + "-" + blogPost.getId();
        return new PostSearchDocument(
                id,
                blogPost.getTitle(),
                blogPost.getDescription(),
                blogPost.getMember().getName(),
                blogPost.getCreatedAt(),
                PostType.BLOG
        );
    }

    public static PostSearchDocument from(CafePost cafePost) {
        String id = PostType.CAFE.name() + "-" + cafePost.getId();
        return new PostSearchDocument(
                id,
                cafePost.getTitle(),
                cafePost.getDescription(),
                cafePost.getCafeMember().getNickname(),
                cafePost.getCreatedAt(),
                PostType.CAFE
        );
    }
}
