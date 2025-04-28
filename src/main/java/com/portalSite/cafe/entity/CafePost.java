package com.portalSite.cafe.entity;

import com.portalSite.cafe.dto.CafePostRequest;
import com.portalSite.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cafe_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafePost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_board_id", nullable = false)
    private CafeBoard cafeBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_member_id", nullable = false)
    private CafeMember cafeMember;

    @Column(name = "title",length = 255)
    private String title;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    private CafePost(Cafe cafe, CafeBoard cafeBoard, CafeMember cafeMember, String title, String description) {
        this.cafe = cafe;
        this.cafeBoard = cafeBoard;
        this.cafeMember = cafeMember;
        this.title = title;
        this.description = description;
    }

    public static CafePost of(Cafe cafe, CafeBoard cafeBoard, CafeMember cafeMember, String title, String description) {
        return new CafePost(cafe, cafeBoard, cafeMember, title, description);
    }

    public void update(CafePostRequest cafePostRequest) {
        this.title = cafePostRequest.title();
        this.description = cafePostRequest.description();
    }
}
