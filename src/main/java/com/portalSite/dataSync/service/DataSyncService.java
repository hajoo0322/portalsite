package com.portalSite.dataSync.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portalSite.blog.dto.response.BlogPostResponse;
import com.portalSite.blog.entity.BlogPost;
import com.portalSite.blog.repository.BlogPostRepository;
import com.portalSite.cafe.dto.CafePostResponse;
import com.portalSite.cafe.entity.CafePost;
import com.portalSite.cafe.repository.CafePostRepository;
import com.portalSite.news.dto.response.NewsResponse;
import com.portalSite.news.entity.News;
import com.portalSite.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSyncService {
    private final NewsRepository newsRepository;
    private final BlogPostRepository blogPostRepository;
    private final CafePostRepository cafePostRepository;

    private final ElasticsearchClient esClient;
    private final ObjectMapper objectMapper;

    public void syncAllNews() throws IOException {
        syncBatch(newsRepository::newsFetchBatch,
                "news_index",
                News::getId,
                NewsResponse::from
        );
    }

    public void syncAllBlogPosts() throws IOException {
        syncBatch(
                blogPostRepository::blogPostFetchBatch,
                "blog_post_index",
                BlogPost::getId,
                BlogPostResponse::from
        );
    }

    public void syncAllCafePosts() throws IOException {
        syncBatch(cafePostRepository::cafePostFetchBatch,
                "cafe_post_index",
                CafePost::getId,
                CafePostResponse::from
        );
    }

    public <T, D> void syncBatch(
            Function<Pageable, List<T>> fetchFunction,
            String indexName,
            Function<T, Object> idExtractor,
            Function<T, D> dtoConverter
    ) throws IOException {
        int batchSize = 1000;
        int page = 0;

        while (true) {
            Pageable pageable = PageRequest.of(page, batchSize);
            List<T> batch = fetchFunction.apply(pageable);
            if (batch.isEmpty()) break;

            List<BulkOperation> operations = batch.stream()
                    .map(item -> {
                        D dto = dtoConverter.apply(item);
                        Object id = idExtractor.apply(item);
                        return BulkOperation.of(op -> op
                                .index(idx -> idx
                                        .index(indexName)
                                        .id(String.valueOf(id))
                                        .document(dto)
                                )
                        );
                    })
                    .toList();

            BulkRequest request = BulkRequest.of(b -> b.operations(operations));
            var response = esClient.bulk(request);

            if (response.errors()) {
                log.error("❌ 일부 문서 인덱싱 실패 ({}): {}", indexName, response.items());
            } else {
                log.info("✅ {} 페이지 {} 전송 성공 ({}건)", indexName, page, batch.size());
            }

            page++;
        }
    }
}
