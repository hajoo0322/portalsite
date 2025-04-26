package com.portalSite.blog.controller;

import com.portalSite.blog.dto.request.CreateBlogBoardRequest;
import com.portalSite.blog.dto.request.UpdateBlogBoardRequest;
import com.portalSite.blog.dto.response.BlogBoardResponse;
import com.portalSite.blog.service.BlogBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/blogs/{blogId}/category")
@RequiredArgsConstructor
public class BlogBoardController {

    private final BlogBoardService blogBoardService;

    @PostMapping
    public ResponseEntity<BlogBoardResponse> createBlogBoard(@RequestBody @Valid CreateBlogBoardRequest request, @PathVariable Long blogId) {
        BlogBoardResponse response = blogBoardService.saveBlogBoard(request, blogId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BlogBoardResponse>> getAllBlogBoard(@PathVariable Long blogId) {
        List<BlogBoardResponse> response = blogBoardService.getAllBlogBoard(blogId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{blogBoardId}")
    public ResponseEntity<BlogBoardResponse> updateBlogBoard(@RequestBody @Valid UpdateBlogBoardRequest request, @PathVariable Long blogBoardId) {
        BlogBoardResponse response = blogBoardService.updateBlogBoard(request, blogBoardId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{blogBoardId}")
    public ResponseEntity<Void> deleteBlogBoard(@PathVariable Long blogBoardId) {
        blogBoardService.deleteBlogBoard(blogBoardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

