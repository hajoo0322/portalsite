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

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @Column(name = "grade")
    private String grade;

    @Column(name = "description")
    private String description;

    @Column(name = "auto_level")
    private Boolean autoLevel;

    @Column(name = "grade_order")
    private Integer gradeOrder;

    private CafeLevel(Cafe cafe, String grade, String description, Boolean autoLevel, Integer gradeOrder) {
        this.cafe = cafe;
        this.grade = grade;
        this.description = description;
        this.autoLevel = autoLevel;
        this.gradeOrder = gradeOrder;
    }

    public static CafeLevel of(Cafe cafe, String grade, String description, Boolean autoLevel, Integer gradeOrder) {
        return new CafeLevel(cafe, grade, description, autoLevel, gradeOrder);
    }

    public void update(String grade, String description, Boolean autoLevel) {
        this.grade = grade;
        this.description = description;
        this.autoLevel = autoLevel;
    }
}
