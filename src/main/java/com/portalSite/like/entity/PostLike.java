package com.portalSite.like.entity;

import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.cafe.entity.CafePost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "post_like")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {
    @Id
    @Column(name = "like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cafe_post_id", nullable = false)
    private CafePost cafePost;

    @ManyToOne
    @JoinColumn(name = "cafe_member_id", nullable = false)
    private CafeMember cafeMember;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private PostLike(CafePost cafePost, CafeMember cafeMember) {
        this.cafePost = cafePost;
        this.cafeMember = cafeMember;
    }

    public static PostLike of(CafePost cafePost, CafeMember cafeMember) {
        return new PostLike(cafePost, cafeMember);
    }
}
