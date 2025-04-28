package com.portalSite.news.service;

import com.portalSite.member.entity.Member;
import com.portalSite.member.repository.MemberRepository;
import com.portalSite.news.dto.request.NewsCreateRequest;
import com.portalSite.news.dto.request.NewsRequest;
import com.portalSite.news.entity.News;
import com.portalSite.news.entity.NewsCategory;
import com.portalSite.news.repository.NewsCategoryRepository;
import com.portalSite.news.repository.NewsRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class NewsService {

  private final NewsRepository newsRepository;
  private final MemberRepository memberRepository;
  private final NewsCategoryRepository newsCategoryRepository;

  @Transactional
  public News createNews(NewsCreateRequest requestDto, Long memberId) {
    Member member = memberRepository.findById(memberId).orElseThrow(
        () -> new RuntimeException() /*TODO 예외 처리 추가(존재하지 않는 작성자)*/
    );

    NewsCategory newsCategory = newsCategoryRepository.findById(requestDto.categoryId())
        .orElseThrow(
            () -> new RuntimeException() /*TODO 예외 처리 추가(존재하지 않는 카테고리)*/
        );

    News news = News.of(member, newsCategory, requestDto.newsTitle(), requestDto.description());

    return newsRepository.save(news);
  }

  @Transactional(readOnly = true)
  public News getNewsById(Long newsId) {
    return newsRepository.findById(newsId).orElseThrow(
        () -> new RuntimeException() /*TODO 예외 처리: (잘못된 리소스, 기사가 존재하지 않음)*/
    );
  }

  @Transactional(readOnly = true)
  public List<News> getNewsListByCategory(Long categoryId) {
    return newsRepository.findAllByNewsCategoryId(categoryId);
  }

  @Transactional
  public News updateNews(NewsRequest requestDto, Long newsId, Long memberId) {
    Member member = memberRepository.findById(memberId).orElseThrow(
        () -> new RuntimeException() /*TODO 예외 처리 추가(수정으로 요청한 사용자를 찾을 수 없음)*/
    );

    News news = getNewsById(newsId);
    if (!memberId.equals(news.getMember().getId())) {
      throw new RuntimeException(); /*TODO 예외처리: 뉴스 작성자와 수정 요청자가 다름(수정권한 없음)*/
    }

    NewsCategory newsCategory = newsCategoryRepository.findById(requestDto.categoryId())
        .orElseThrow(
            () -> new RuntimeException() /*TODO 예외 처리 추가(존재하지 않는 카테고리)*/
        );

    news.updateNews(newsCategory, requestDto.newsTitle(), requestDto.description());

    return news;
  }

  @Transactional
  public void deleteNews(Long newsId, Long memberId) {
    News news = getNewsById(newsId);
    if (!memberId.equals(news.getMember().getId())) {
      throw new RuntimeException(); /*TODO 예외처리: 뉴스 작성자와 삭제 요청자가 다름(삭제권한 없음)*/
    }

    newsRepository.delete(news);
  }
}
