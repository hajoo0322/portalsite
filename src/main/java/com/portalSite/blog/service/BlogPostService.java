package com.portalSite.blog.service;

import com.portalSite.blog.dto.request.CreateBlogPostRequest;
import com.portalSite.blog.dto.request.UpdateBlogPostRequest;
import com.portalSite.blog.dto.response.BlogPostResponse;
import com.portalSite.blog.entity.Blog;
import com.portalSite.blog.entity.BlogBoard;
import com.portalSite.blog.entity.BlogPost;
import com.portalSite.blog.repository.BlogBoardRepository;
import com.portalSite.blog.repository.BlogPostRepository;
import com.portalSite.blog.repository.BlogRepository;
import com.portalSite.member.entity.Member;
import com.portalSite.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation;
import org.springframework.transaction.annotation.Transactional;

@Service

@RequiredArgsConstructor
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final BlogBoardRepository blogBoardRepository;
    private final BlogRepository blogRepository;
    private final MemberRepository memberRepository;


    public BlogPostResponse saveBlogPost(CreateBlogPostRequest request, Long blogId, Long blogBoardId, Long memberId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new RuntimeException(""));
        BlogBoard blogBoard = blogBoardRepository.findById(blogBoardId).orElseThrow(() -> new RuntimeException(""));
        Member blogMember = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException(""));
        BlogPost blogPost = BlogPost.of(blog, blogBoard, blogMember, request.title(), request.description());
        BlogPost savedBlogPost = blogPostRepository.save(blogPost);
        return BlogPostResponse.of(savedBlogPost);
    }

    @Transactional(readOnly = true)
    public List<BlogPostResponse> getAllBlogPostByBlogId(Long blogId) {
        List<BlogPost> blogPostList = blogPostRepository.findAllByBlogId(blogId);
        if (blogPostList.isEmpty()) {
            throw new RuntimeException("");
        }
        return blogPostList.stream().map(BlogPostResponse::of).toList();
    }


    public BlogPostResponse updateBlogPost(UpdateBlogPostRequest request, Long blogPostId) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId).orElseThrow(() -> new RuntimeException(""));
        blogPost.update(request);
        BlogPost savedBlogPost = blogPostRepository.save(blogPost);
        return BlogPostResponse.of(savedBlogPost);
    }

    public void deleteBlogPost(Long blogPostId) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId).orElseThrow(() -> new RuntimeException(""));
        blogPostRepository.delete(blogPost);
    }
}