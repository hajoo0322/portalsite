package com.portalSite.comment.entity;

import com.portalSite.cafe.entity.Cafe;
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
@Table(name = "comment_alarm")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentAlarm {

    @Id
    @Column(name = "comment_alarm_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cafe_member_id")
    private CafeMember cafeMember;

    @ManyToOne
    @JoinColumn(name = "cafe_post_id")
    private CafePost cafePost;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private CommentAlarm(CafeMember cafeMember, CafePost cafePost, LocalDateTime createdAt) {
        this.cafeMember = cafeMember;
        this.cafePost = cafePost;
        this.createdAt = createdAt;
    }

    public static CommentAlarm of(CafeMember cafeMember, CafePost cafePost) {
        return new CommentAlarm(cafeMember, cafePost, LocalDateTime.now());
    }
}
