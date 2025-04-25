package com.portalSite.cafe.entity;

import com.portalSite.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "cafe")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafe extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "cafe_name", length = 25)
    private String cafeName;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private CafeRanking cafeRanking;


    private Cafe(String cafeName, String description, CafeRanking cafeRanking) {
        this.cafeName = cafeName;
        this.description = description;
        this.cafeRanking = cafeRanking;
    }

    public static Cafe of(String cafeName, String description) {
        return new Cafe(cafeName, description, CafeRanking.BRONZE);
    }

    public void changeCafeRanking(CafeRanking cafeRanking) {
        this.cafeRanking = cafeRanking;
    }
}
