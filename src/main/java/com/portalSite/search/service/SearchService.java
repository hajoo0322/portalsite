package com.portalSite.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portalSite.blog.dto.response.BlogPostResponse;
import com.portalSite.blog.repository.BlogPostRepository;
import com.portalSite.cafe.dto.CafePostResponse;
import com.portalSite.cafe.repository.CafePostRepository;
import com.portalSite.comment.entity.PostType;
//import com.portalSite.common.infra.redis.RedisService;
import com.portalSite.news.dto.response.NewsResponse;
import com.portalSite.news.repository.NewsRepository;

import com.portalSite.search.dto.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final BlogPostRepository blogPostRepository;
    private final CafePostRepository cafePostRepository;
    private final NewsRepository newsRepository;
    private final ElasticsearchClient esClient;
//    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    /**
     * 검색 기준<br>
     * 카페, 뉴스 : 제목, 내용<br>
     * 블로그 : 제목, 내용
     */
    public SearchResponse searchV3(
            String keyword, String writer, LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd, PostType postType, Pageable pageable) {

        Sort.Direction direction = pageable.getSort()
                .stream().findFirst().map(Sort.Order::getDirection)
                .orElse(Sort.Direction.DESC);

        Object cached = redisService.getSearchResult(keyword, writer, createdAtStart, createdAtEnd,
                postType, pageable.getPageNumber(), pageable.getPageSize(), direction);
        if (cached != null) {
            return objectMapper.convertValue(cached, SearchResponse.class);
        }

        List<BlogPostResponse> blogPostList = postType == null || postType == PostType.BLOG
                ? blogPostRepository.findAllByKeywordV2(
                        keyword, writer, createdAtStart, createdAtEnd, pageable).getContent()
                : null;

        List<CafePostResponse> cafePostList = postType == null || postType == PostType.CAFE
                ? cafePostRepository.findAllByKeywordV2(
                        keyword, writer, createdAtStart, createdAtEnd, pageable).getContent()
                : null;

        List<NewsResponse> newsList = postType == null || postType == PostType.NEWS
                ? newsRepository.findAllByKeywordV2(
                        keyword, writer, createdAtStart, createdAtEnd, pageable).getContent()
                : null;
        SearchResponse response = SearchResponse.from(blogPostList, cafePostList, newsList, pageable);

        redisService.cacheSearchResult(keyword, writer, createdAtStart, createdAtEnd,
                postType, pageable.getPageNumber(), pageable.getPageSize(), direction, response);

        return response;
    }

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

    public SearchResponse searchV2(String keyword, Pageable pageable, PostType postType) {

        List<BlogPostResponse> blogPostList = postType == null || postType == PostType.BLOG ?
                blogPostRepository.findAllByKeywordWithIndex(keyword, pageable).stream().toList() : null;

        List<CafePostResponse> cafePostList = postType == null || postType == PostType.CAFE ?
                cafePostRepository.findAllByKeywordWithIndex(keyword, pageable).stream().toList() : null;

        List<NewsResponse> newsList = postType == null || postType == PostType.NEWS ?
                newsRepository.findAllByKeywordWithIndex(keyword, pageable).stream().toList() : null;

        return SearchResponse.from(blogPostList, cafePostList, newsList, pageable);
    }

    public SearchResponse searchByElasticsearch(String keyword, Pageable pageable, PostType postType) {
        List<BlogPostResponse> blogPostList = (postType == null || postType == PostType.BLOG) ?
                searchGeneric("blog_post_index", keyword, List.of("title", "description"), pageable, BlogPostResponse.class, "id") : null;

        List<CafePostResponse> cafePostList = (postType == null || postType == PostType.CAFE) ?
                searchGeneric("cafe_post_index", keyword, List.of("title", "description"), pageable, CafePostResponse.class, "id") : null;

        List<NewsResponse> newsList = (postType == null || postType == PostType.NEWS) ?
                searchGeneric("news_index", keyword, List.of("newsTitle", "description"), pageable, NewsResponse.class, "newsId") : null;

        return SearchResponse.from(blogPostList, cafePostList, newsList, pageable);
    }

    private <T> List<T> searchGeneric(String indexName, String keyword, List<String> fields, Pageable pageable, Class<T> responseClass, String sortField) {
        try{
            //ES 검색 및 결과는 Class<T>로 바로 매핑
            var response = esClient.search(s->s
                            .index(indexName)  //검색할 인덱스 지정
                            .query(q->q  //검색 조건 설정.  multi_match 쿼리로 여러 필드에 대해 검색
                                    .multiMatch(m->m
                                            .fields(fields)
                                            .query(keyword)
                                    )
                            )
                            .from((int) pageable.getOffset())  //페이징 처리
                            .size(pageable.getPageSize())
                            .sort(srt -> srt
                                    .field(f->f.field(sortField).order(SortOrder.Desc))  //id를 기준으로 내림차순 정렬
                            ),
                    responseClass //결과 매핑
            );

            return response.hits().hits().stream().map(Hit::source).toList();  //ES 에서 주요내용(Class<T>)만 리스트로 반환
        }catch (IOException e){
            throw new RuntimeException("ES 검색 실패. indexName = "+indexName, e);
        }
    }

}
