package com.portalSite.news.repository;

import com.portalSite.news.entity.NewsCategory;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {

  Optional<NewsCategory> findByName(String name);

  List<NewsCategory> findAllByParentId(Long categoryId);
}
