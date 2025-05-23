package com.portalSite.news.entity;

import com.portalSite.common.BaseEntity;

import com.portalSite.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "news", indexes = {
		@Index(name = "idx_news_created_at", columnList = "created_at")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "news_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
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

	public void updateNews(NewsCategory newsCategory, String newsTitle, String description){
		if(newsCategory!=null) this.newsCategory=newsCategory;
		if(newsTitle!=null) this.newsTitle=newsTitle;
		if(description!=null) this.description=description;
	}
}
