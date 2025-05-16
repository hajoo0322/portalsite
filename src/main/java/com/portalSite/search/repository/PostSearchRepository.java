package com.portalSite.search.repository;

import com.portalSite.search.document.PostSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository extends ElasticsearchRepository<PostSearchDocument, String> {
}
