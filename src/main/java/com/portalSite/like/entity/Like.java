package com.portalSite.like.entity;

import com.portalSite.cafe.entity.CafeMember;
import com.portalSite.cafe.entity.CafePost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {
    @Id
    @Column(name = "like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Column(name = "cafe_post_id", nullable = false)
    private CafePost cafePost;

    @ManyToOne
    @Column(name = "cafe_member_id", nullable = false)
    private CafeMember cafeMember;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Like(CafePost cafePost, CafeMember cafeMember, LocalDateTime createdAt) {
        this.cafePost = cafePost;
        this.cafeMember = cafeMember;
        this.createdAt = createdAt;
    }

    public static Like of(CafePost cafePost, CafeMember cafeMember) {
        return new Like(cafePost, cafeMember, LocalDateTime.now());
    }
}
