package com.portalSite.blog.controller;


import com.portalSite.blog.dto.request.CreateBlogPostRequest;
import com.portalSite.blog.dto.request.UpdateBlogPostRequest;
import com.portalSite.blog.dto.response.BlogPostResponse;
import com.portalSite.blog.service.BlogPostService;
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

@RestController
@RequestMapping("api/member/{memberId}/blog/{blogId}/category/{blogBoardId}")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @PostMapping
    public ResponseEntity<BlogPostResponse> createBlogPost( @RequestBody CreateBlogPostRequest request,
                                                            @PathVariable Long blogId,
                                                            @PathVariable Long blogBoardId,
                                                            @PathVariable Long memberId) {
        BlogPostResponse blogPostResponse = blogPostService.saveBlogPost(request, blogId, blogBoardId,memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(blogPostResponse);
    }

    @GetMapping
    public ResponseEntity<List<BlogPostResponse>> getAllBlogPostByCategoryId(@PathVariable Long blogId) {
        List<BlogPostResponse> blogPostResponseList = blogPostService.getAllBlogPostByBlogId(blogId);
        return ResponseEntity.status(HttpStatus.OK).body(blogPostResponseList);
    }

    @GetMapping("/{blogPostId}")
    public ResponseEntity<List<BlogPostResponse>> getBlogPost(@PathVariable Long blogId) {
        List<BlogPostResponse> blogPostResponseList = blogPostService.getAllBlogPostByBlogId(blogId);
        return ResponseEntity.status(HttpStatus.OK).body(blogPostResponseList);
    }

    @PatchMapping("/{blogPostId}")
    public ResponseEntity<BlogPostResponse> updateBlogPost(@RequestBody UpdateBlogPostRequest request, @PathVariable Long blogPostId) {
        BlogPostResponse blogPostResponse = blogPostService.updateBlogPost(request, blogPostId);
        return ResponseEntity.status(HttpStatus.OK).body(blogPostResponse);
    }

    @DeleteMapping("/{blogPostId}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable Long blogPostId) {
        blogPostService.deleteBlogPost(blogPostId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
