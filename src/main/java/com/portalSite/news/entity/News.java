package com.portalSite.news.entity;

import com.portalSite.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "news_id")
	private Long id;

	@OneToMany
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "news_category_id")
	private NewsCategory newsCategory;

	@Column(name = "news_title", length = 100)
	private String newsTitle;

	@Column(columnDefinition = "TEXT")
	private String description;

	private News(Member member, NewsCategory newsCategory, String newsTitle, String description) {
		this.member = member;
		this.newsCategory = newsCategory;
		this.newsTitle = newsTitle;
		this.description = description;
	}

	public static News of(Member member, NewsCategory newsCategory, String newsTitle, String description) {
		return new News(member, newsCategory, newsTitle, description);
	}
}
