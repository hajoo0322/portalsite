package com.portalSite.news.service;

import com.portalSite.news.dto.request.NewsCategoryRequest;
import com.portalSite.news.dto.response.NewsCategoryListResponse;
import com.portalSite.news.dto.response.NewsCategoryResponse;
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
    public NewsCategoryResponse createCategory(@Valid NewsCategoryRequest request) {
        if (newsCategoryRepository.findByName(request.name()).isPresent()) {
            throw new RuntimeException(); /*TODO 예외처리 (이미 존재하는 카테고리)*/
        }

        NewsCategory parentCategory = null;
        if (null != request.parentId()) {
            parentCategory = newsCategoryRepository.findById(request.parentId()).orElseThrow(
                    () -> new RuntimeException() /*TODO 예외처리 (상위 카테고리를 찾을 수 없음)*/
            );
        }

        return NewsCategoryResponse.from(newsCategoryRepository.save(NewsCategory.of(request.name(), parentCategory)));
    }

    @Transactional(readOnly = true)
    public NewsCategoryListResponse getSubCategoriesByParentId(Long parentId) {
        if (!newsCategoryRepository.existsById(parentId)) {
            throw new RuntimeException(); /*TODO 예외처리(존재하지 않는 카테고리)*/
        }

        List<NewsCategoryResponse> newsList = newsCategoryRepository.findAllByParentId(parentId).stream()
                .map(NewsCategoryResponse::from)
                .toList();

        if (newsList.isEmpty()) {
            return NewsCategoryListResponse.from("해당 카테고리에 존재하는 뉴스가 없습니다.", null);
        }
        return NewsCategoryListResponse.from("뉴스 조회 성공", newsList);
    }

    @Transactional
    public NewsCategory updateCategory(NewsCategoryRequest request, Long categoryId) {
        NewsCategory newsCategory = newsCategoryRepository.findById(categoryId).orElseThrow(
                () -> new RuntimeException() /*TODO 예외처리(존재하지 않는 카테고리)*/
        );

        NewsCategory parentCategory = null;
        if (null != request.parentId()) {
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

    @Transactional
    public void deleteSubcategories(Long parentId) {
        List<NewsCategory> subCategoryList = newsCategoryRepository.findAllByParentId(parentId);

        if (subCategoryList.isEmpty()) {
            return;
        }

        for (NewsCategory newsCategory : subCategoryList) {
            deleteSubcategories(newsCategory.getId());
            newsCategoryRepository.delete(newsCategory); //관련 하위 카테고리 모두 삭제
        }
    }
}
