package com.portalSite.cafe.entity;

import com.portalSite.cafe.dto.CafeBoardRequest;
import com.portalSite.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cafe_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id",nullable = false)
    private Cafe cafe;

    @Column(name = "board_name",length = 30)
    private String boardName;


    private CafeBoard(Cafe cafe, String boardName) {
        this.cafe = cafe;
        this.boardName = boardName;
    }

    public static CafeBoard of(Cafe cafe, String boardName) {
        return new CafeBoard(cafe, boardName);
    }

    public void update(CafeBoardRequest cafeBoard) {
        this.boardName = cafeBoard.boardName();
    }
}
