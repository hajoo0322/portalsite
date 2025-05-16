package com.portalSite.search.repository;

import com.portalSite.search.dto.response.PostSearchResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public class PostSearchRepositoryImpl implements PostSearchRepositoryCustom{
    @Override
    public PostSearchResponse findByKeywordAndOther(
            String keyword, String writer, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Pageable pageable) {

        return null;
    }
}
