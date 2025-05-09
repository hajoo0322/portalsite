package com.portalSite.news.service;

import com.portalSite.common.exception.custom.CustomException;
import com.portalSite.common.exception.custom.ErrorCode;
import com.portalSite.news.dto.request.NewsCategoryRequest;
import com.portalSite.news.dto.response.NewsCategoryResponse;
import com.portalSite.news.entity.NewsCategory;
import com.portalSite.news.repository.NewsCategoryRepository;
import jakarta.validation.Valid;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class NewsCategoryService {

    private final NewsCategoryRepository newsCategoryRepository;

    @Transactional
    public NewsCategoryResponse createCategory(@Valid NewsCategoryRequest request) {
        if (newsCategoryRepository.findByName(request.name()).isPresent()) {
            throw new CustomException(ErrorCode.NEWS_CATEGORY_ALREADY_EXIST);
        }

        NewsCategory parentCategory = null;
        if (null != request.parentId()) {
            parentCategory = newsCategoryRepository.findById(request.parentId()).orElseThrow(
                    () -> new CustomException(ErrorCode.PARENT_CATEGORY_NOT_FOUND)
            );
        }

        return NewsCategoryResponse.from(newsCategoryRepository.save(NewsCategory.of(request.name(), parentCategory)));
    }

    @Transactional(readOnly = true)
    public List<NewsCategoryResponse> getSubCategoriesByParentId(Long parentId, Pageable pageable) {
        if (!newsCategoryRepository.existsById(parentId)) {
            throw new CustomException(ErrorCode.NEWS_CATEGORY_NOT_FOUND);
        }

        List<NewsCategoryResponse> newsCategoryList = newsCategoryRepository.findByParentId(parentId, pageable).stream()
                .map(NewsCategoryResponse::from)
                .toList();

        if (newsCategoryList.isEmpty()) {
            throw new CustomException(ErrorCode.CHILD_CATEGORY_NOT_FOUND);
        }

        return newsCategoryList;
    }

    @Transactional
    public NewsCategory updateCategory(NewsCategoryRequest request, Long categoryId) {
        NewsCategory newsCategory = newsCategoryRepository.findById(categoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NEWS_CATEGORY_NOT_FOUND)
        );

        NewsCategory parentCategory = null;
        if (null != request.parentId()) {
            parentCategory = newsCategoryRepository.findById(request.parentId()).orElseThrow(
                    () -> new CustomException(ErrorCode.PARENT_CATEGORY_NOT_FOUND)
            );
        }

        newsCategory.updateCategory(request.name(), parentCategory);

        return newsCategory;
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        NewsCategory newsCategory = newsCategoryRepository.findById(categoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NEWS_CATEGORY_NOT_FOUND)
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
