package com.portalSite.blog.controller;

import com.portalSite.blog.dto.request.CreateBlogRequest;
import com.portalSite.blog.dto.request.UpdateBlogRequest;
import com.portalSite.blog.dto.response.BlogResponse;
import com.portalSite.blog.service.BlogService;
import com.portalSite.security.AuthUser;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blogs")
public class BlogController {

    private final BlogService blogService;

    @PostMapping
    public ResponseEntity<BlogResponse> createBlog( @RequestBody @Valid CreateBlogRequest request,
                                                    @AuthenticationPrincipal AuthUser member) {
        BlogResponse response = blogService.saveBlog(request, member.memberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BlogResponse>> getAllBlogs() {
        List<BlogResponse> response = blogService.getAllBlogs();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<BlogResponse> getBlog(@PathVariable Long blogId) {
        BlogResponse response = blogService.getBlog(blogId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{blogId}")
    public ResponseEntity<BlogResponse> updateBlog( @RequestBody @Valid UpdateBlogRequest request,
                                                    @PathVariable Long blogId,
                                                    @AuthenticationPrincipal AuthUser member) {
        BlogResponse response = blogService.updateBlog(request, blogId, member.memberId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<Void> deleteBlog( @PathVariable Long blogId,
                                            @AuthenticationPrincipal AuthUser member) {
        blogService.deleteBlog(blogId, member.memberId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
