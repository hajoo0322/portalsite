package com.portalSite.cafe.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cafe_level")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

    @Column(name = "grade", length = 12)
    private String grade;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "auto_level", nullable = false)
    private Boolean autoLevel;

    @Column(name = "grade_order", nullable = false)
    private Integer gradeOrder;

    @Column(name = "level_visit_count", nullable = false)
    private int levelVisitCount;

    @Column(name = "level_comment_count", nullable = false)
    private int levelCommentCount;

    @Column(name = "level_post_count", nullable = false)
    private int levelPostCount;

    private CafeLevel(Cafe cafe, String grade, String description, Boolean autoLevel, Integer gradeOrder,
                      int levelVisitCount, int levelCommentCount, int levelPostCount) {
        this.cafe = cafe;
        this.grade = grade;
        this.description = description;
        this.autoLevel = autoLevel;
        this.gradeOrder = gradeOrder;
        this.levelVisitCount = levelVisitCount;
        this.levelCommentCount = levelCommentCount;
        this.levelPostCount = levelPostCount;
    }

    public static CafeLevel of(Cafe cafe, String grade, String description, Boolean autoLevel, Integer gradeOrder,
                               int levelVisitCount, int levelCommentCount, int levelPostCount) {
        return new CafeLevel(cafe, grade, description, autoLevel, gradeOrder, levelVisitCount, levelCommentCount, levelPostCount);
    }

    public void update(String grade, String description, Boolean autoLevel, int levelVisitCount, int levelCommentCount, int levelPostCount) {
        this.grade = grade;
        this.description = description;
        this.autoLevel = autoLevel;
        this.levelVisitCount = levelVisitCount;
        this.levelCommentCount = levelCommentCount;
        this.levelPostCount = levelPostCount;
    }
}
