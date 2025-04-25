package com.portalSite.cafe.entity;

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

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @Column(name = "board_name")
    private String boardName;

}
