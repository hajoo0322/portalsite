package com.portalSite.blog.controller;


import com.portalSite.blog.dto.request.CreateBlogPostRequest;
import com.portalSite.blog.dto.request.UpdateBlogPostRequest;
import com.portalSite.blog.dto.response.BlogPostResponse;
import com.portalSite.blog.service.BlogPostService;
import com.portalSite.security.AuthUser;
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
@RequestMapping("/blogs/{blogId}/categories/{blogBoardId}/posts")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @PostMapping
    public ResponseEntity<BlogPostResponse> createBlogPost( @RequestBody CreateBlogPostRequest request,
                                                            @PathVariable Long blogId,
                                                            @PathVariable Long blogBoardId,
                                                            @AuthenticationPrincipal AuthUser member) {
        BlogPostResponse response = blogPostService.saveBlogPost(request, blogId, blogBoardId,member.memberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    @GetMapping
//    public ResponseEntity<List<BlogPostResponse>> getAllBlogPost(@PathVariable Long blogId) {
//        List<BlogPostResponse> response = blogPostService.getAllBlogPostByBlogId(blogId);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }

    @GetMapping
    public ResponseEntity<List<BlogPostResponse>> getAllBlogPostByBlogBoardId(@PathVariable Long blogBoardId) {
        List<BlogPostResponse> response = blogPostService.getAllBlogPostByBlogBoardId(blogBoardId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{blogPostId}")
    public ResponseEntity<BlogPostResponse> getBlogPost(@PathVariable Long blogPostId) {
        BlogPostResponse response = blogPostService.getBlogPostById(blogPostId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{blogPostId}")
    public ResponseEntity<BlogPostResponse> updateBlogPost( @RequestBody UpdateBlogPostRequest request,
                                                            @PathVariable Long blogPostId,
                                                            @AuthenticationPrincipal AuthUser member) {
        BlogPostResponse response = blogPostService.updateBlogPost(request, blogPostId, member.memberId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{blogPostId}")
    public ResponseEntity<Void> deleteBlogPost( @PathVariable Long blogPostId,
                                                @AuthenticationPrincipal AuthUser member) {
        blogPostService.deleteBlogPost(blogPostId, member.memberId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
