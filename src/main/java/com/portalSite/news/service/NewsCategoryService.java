package com.portalSite.news.service;

import com.portalSite.news.dto.request.NewsCategoryRequest;
import com.portalSite.news.dto.response.NewsCategoryResponse;
import com.portalSite.news.entity.News;
import com.portalSite.news.entity.NewsCategory;
import com.portalSite.news.repository.NewsCategoryRepository;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class NewsCategoryService {

  private final NewsCategoryRepository newsCategoryRepository;

  @Transactional
  public NewsCategory createCategory(@Valid NewsCategoryRequest request) {
    if (newsCategoryRepository.findByName(request.name()).isPresent()) {
      throw new RuntimeException(); /*TODO 예외처리 (이미 존재하는 카테고리)*/
    }

    NewsCategory parentCategory = null;
    if (null!=request.parentId()) {
      parentCategory = newsCategoryRepository.findById(request.parentId()).orElseThrow(
          () -> new RuntimeException() /*TODO 예외처리 (상위 카테고리를 찾을 수 없음)*/
      );
    }

    return newsCategoryRepository.save(NewsCategory.of(request.name(), parentCategory));
  }

  @Transactional(readOnly = true)
  public List<NewsCategory> getSubCategoriesByParentId(Long categoryId) {

    return newsCategoryRepository.findAllByParentId(categoryId);
  }

  @Transactional
  public NewsCategory updateCategory(NewsCategoryRequest request, Long categoryId) {
    NewsCategory newsCategory = newsCategoryRepository.findById(categoryId).orElseThrow(
        () -> new RuntimeException() /*TODO 예외처리(존재하지 않는 카테고리)*/
    );

    NewsCategory parentCategory = null;
    if(null!=request.parentId()) {
      parentCategory = newsCategoryRepository.findById(request.parentId()).orElseThrow(
          () -> new RuntimeException() /*TODO 예외처리(상위 카테고리를 찾을 수 없음)*/
      );
    }

    newsCategory.updateCategory(request.name(), parentCategory);

    return newsCategory;
  }

  @Transactional
  public void deleteCategory(Long categoryId) {
    NewsCategory newsCategory = newsCategoryRepository.findById(categoryId).orElseThrow(
        () -> new RuntimeException() /*TODO 예외처리(존재하지 않는 카테고리)*/
    );

    deleteSubcategories(categoryId);

    newsCategoryRepository.delete(newsCategory); //최상위 카테고리 삭제
  }

  private void deleteSubcategories(Long parentId) {
    List<NewsCategory> subCategoryList = getSubCategoriesByParentId(parentId);

    if(subCategoryList.isEmpty()) return;

    for (NewsCategory newsCategory : subCategoryList){
      deleteSubcategories(newsCategory.getId());
      newsCategoryRepository.delete(newsCategory); //관련 하위 카테고리 모두 삭제
    }
  }
}
