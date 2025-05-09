package com.portalSite.news.controller;

import com.portalSite.member.entity.MemberRole;
import com.portalSite.news.dto.request.NewsCategoryRequest;
import com.portalSite.news.dto.response.NewsCategoryResponse;
import com.portalSite.news.service.NewsCategoryService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news/categories")
public class NewsCategoryController {

  private final NewsCategoryService newsCategoryService;

  @PostMapping()
  @Secured(MemberRole.Authority.ADMIN)
  public ResponseEntity<NewsCategoryResponse> createCategory(
      @Valid @RequestBody NewsCategoryRequest request
  ) {
    NewsCategoryResponse response = newsCategoryService.createCategory(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{categoryId}/subcategories")
  public ResponseEntity<List<NewsCategoryResponse>> getSubCategoriesByParentId(
      @PathVariable Long categoryId,
      @PageableDefault(sort = "id", direction = DESC) Pageable pageable
  ) {
    List<NewsCategoryResponse> response = newsCategoryService.getSubCategoriesByParentId(categoryId, pageable);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PatchMapping("/{categoryId}")
  @Secured(MemberRole.Authority.ADMIN)
  public ResponseEntity<NewsCategoryResponse> updateCategory(
    @RequestBody NewsCategoryRequest request,
    @PathVariable Long categoryId
  ){
    NewsCategoryResponse response = NewsCategoryResponse.from(newsCategoryService.updateCategory(request, categoryId));

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{categoryId}")
  @Secured(MemberRole.Authority.ADMIN)
  public ResponseEntity<Void> deleteCategory(
      @PathVariable Long categoryId
  ){
    newsCategoryService.deleteCategory(categoryId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
