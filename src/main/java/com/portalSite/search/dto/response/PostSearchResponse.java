package com.portalSite.search.dto.response;

import com.portalSite.search.document.PostSearchDocument;
import org.springframework.data.domain.Pageable;

import java.util.List;

public record PostSearchResponse(
        List<PostSearchDocument> cafePostDocuments,
        List<PostSearchDocument> blogPostDocuments,
        List<PostSearchDocument> newsDocuments,
        int page,
        int size
) {
    public static PostSearchResponse from(
            List<PostSearchDocument> cafePostDocuments,
            List<PostSearchDocument> blogPostDocuments,
            List<PostSearchDocument> newsDocuments,
            Pageable pageable) {
        return new PostSearchResponse(
                cafePostDocuments,
                blogPostDocuments,
                newsDocuments,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }
}
