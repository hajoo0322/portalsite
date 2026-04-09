package com.portalSite.search.dto.response;

import com.portalSite.blog.dto.response.BlogPostResponse;
import com.portalSite.cafe.dto.CafePostResponse;
import com.portalSite.news.dto.response.NewsResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public record SliceSearchResponse(
        List<BlogPostResponse> blogPostList,
        boolean blogHasNext,
        List<CafePostResponse> cafePostList,
        boolean cafeHasNext,
        List<NewsResponse> newsList,
        boolean newsHasNext,
        int page,
        int size
) {
    public static SliceSearchResponse from(
            Slice<BlogPostResponse> blogPostSlice,
            Slice<CafePostResponse> cafePostSlice,
            Slice<NewsResponse> newsSlice,
            Pageable pageable) {
        return new SliceSearchResponse(
                blogPostSlice != null ? blogPostSlice.getContent() : null,
                blogPostSlice != null && blogPostSlice.hasNext(),
                cafePostSlice != null ? cafePostSlice.getContent() : null,
                cafePostSlice!=null && cafePostSlice.hasNext(),
                newsSlice != null ? newsSlice.getContent() : null,
                newsSlice!=null && newsSlice.hasNext(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }
}
