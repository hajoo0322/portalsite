package com.portalSite.cafe.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cafeName;

    private String description;

    private CafeRanking cafeRanking;


    private Cafe(String cafeName, String description, CafeRanking cafeRanking) {
        this.cafeName = cafeName;
        this.description = description;
        this.cafeRanking = cafeRanking;
    }

    public static Cafe of(String cafeName,String description) {
        return new Cafe(cafeName, description, CafeRanking.BRONZE);
    }

    public void changeCafeRanking(CafeRanking cafeRanking) {
        this.cafeRanking = cafeRanking;
    }
}
