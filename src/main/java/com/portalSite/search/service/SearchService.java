package com.portalSite.search.service;

import com.portalSite.blog.dto.response.BlogPostResponse;
import com.portalSite.blog.repository.BlogPostRepository;
import com.portalSite.cafe.dto.CafePostResponse;
import com.portalSite.cafe.repository.CafePostRepository;
import com.portalSite.comment.entity.PostType;
import com.portalSite.news.dto.response.NewsResponse;
import com.portalSite.news.repository.NewsRepository;
import com.portalSite.search.dto.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final BlogPostRepository blogPostRepository;
    private final CafePostRepository cafePostRepository;
    private final NewsRepository newsRepository;

    public SearchResponse search(String keyword, Pageable pageable, PostType postType) {

        List<BlogPostResponse> blogPostList = postType == null || postType == PostType.BLOG ?
                blogPostRepository.findAllByKeyword(keyword, pageable).stream()
                .map(BlogPostResponse::from).toList() : null;

        List<CafePostResponse> cafePostList = postType == null || postType == PostType.CAFE ?
                cafePostRepository.findAllByKeyword(keyword, pageable).stream()
                .map(CafePostResponse::from).toList() : null;

        List<NewsResponse> newsList = postType == null || postType == PostType.NEWS ?
                newsRepository.findAllByKeyword(keyword, pageable).stream()
                .map(NewsResponse::from).toList() : null;

        return SearchResponse.from(blogPostList, cafePostList, newsList, pageable);
    }
}
