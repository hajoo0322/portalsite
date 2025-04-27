package com.portalSite.news.controller;

import com.portalSite.news.dto.request.NewsCategoryRequest;
import com.portalSite.news.dto.response.NewsCategoryResponse;
import com.portalSite.news.entity.NewsCategory;
import com.portalSite.news.service.NewsCategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*TODO 관리자만 작성 가능하도록 모든 메서드에 대해 권한 추가*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/news/categories")
public class NewsCategoryController {

  private final NewsCategoryService newsCategoryService;

  @PostMapping()
  public ResponseEntity<NewsCategoryResponse> createCategory(
      @Valid @RequestBody NewsCategoryRequest request
  ) {
    NewsCategoryResponse response = NewsCategoryResponse.from(
        newsCategoryService.createCategory(request));

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{categoryId}/subcategories")
  public ResponseEntity<List<NewsCategoryResponse>> getSubCategoriesByParentId(
      @PathVariable Long categoryId
  ) {
    List<NewsCategoryResponse> responseList = newsCategoryService
        .getSubCategoriesByParentId(categoryId).stream()
        .map(NewsCategoryResponse::from)
        .toList();

    return ResponseEntity.status(HttpStatus.OK).body(responseList);
  }

  @PatchMapping("/{categoryId}")
  public ResponseEntity<NewsCategoryResponse> updateCategory(
    @RequestBody NewsCategoryRequest request,
    @PathVariable Long categoryId
  ){
    NewsCategoryResponse response = NewsCategoryResponse.from(newsCategoryService.updateCategory(request, categoryId));

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Null> deleteCategory(
      @PathVariable Long categoryId
  ){
    newsCategoryService.deleteCategory(categoryId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
