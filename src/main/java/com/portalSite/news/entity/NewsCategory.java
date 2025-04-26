package com.portalSite.news.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "news_category_id")
  private Long id;

  @Column(name = "category_name")
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private NewsCategory parent; //상위 카테고리

  @OneToMany(mappedBy = "parent")
  private List<NewsCategory> children = new ArrayList<>(); //하위 카테고리

  private NewsCategory(String name, NewsCategory parent) {
    this.name = name;
    this.parent = parent;
  }

  public static NewsCategory of(String name, NewsCategory parent) {
    return new NewsCategory(name, parent);
  }

  public void updateCategory(String name, NewsCategory parent) {
    if(parent!=null) this.parent=parent;
    if(name!=null) this.name=name;
  }
}
