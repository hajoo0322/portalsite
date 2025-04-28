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
@Table(name = "post_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {
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

    private Like(CafePost cafePost, CafeMember cafeMember, LocalDateTime createdAt) {
        this.cafePost = cafePost;
        this.cafeMember = cafeMember;
        this.createdAt = createdAt;
    }

    public static Like of(CafePost cafePost, CafeMember cafeMember) {
        return new Like(cafePost, cafeMember, LocalDateTime.now());
    }
    // createDate를 썻는데 로컬데이트타임 나우 메서드는 필요없징 크리에이트데이트 쓰려면 클래스에 앤티티리스너있어야함
}
