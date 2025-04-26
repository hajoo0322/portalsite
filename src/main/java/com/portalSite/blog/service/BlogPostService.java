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
import org.springframework.transaction.annotation.Transactional;

@Service

@RequiredArgsConstructor
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final BlogBoardRepository blogBoardRepository;
    private final BlogRepository blogRepository;
    private final MemberRepository memberRepository;


    public BlogPostResponse saveBlogPost(CreateBlogPostRequest request, Long blogId, Long blogBoardId, Long memberId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new RuntimeException("존재하지 않는 블로그입니다."));
        BlogBoard blogBoard = blogBoardRepository.findById(blogBoardId).orElseThrow(() -> new RuntimeException("존재하지 않는 게시판입니다."));
        Member blogMember = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
        BlogPost blogPost = BlogPost.of(blog, blogBoard, blogMember, request.getTitle(), request.getDescription());

        return BlogPostResponse.from(blogPostRepository.save(blogPost));
    }

    @Transactional(readOnly = true)
    public List<BlogPostResponse> getAllBlogPostByBlogId(Long blogId) {
        List<BlogPost> blogPostList = blogPostRepository.findAllByBlogId(blogId);

        return blogPostList.stream().map(BlogPostResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<BlogPostResponse> getAllBlogPostByBlogBoardId(Long blogboardId) {
        List<BlogPost> blogPostList = blogPostRepository.findAllByBlogBoardId(blogboardId);

        return blogPostList.stream().map(BlogPostResponse::from).toList();
    }

    public BlogPostResponse updateBlogPost(UpdateBlogPostRequest request, Long blogPostId) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId).orElseThrow(() -> new RuntimeException("존재하지 않는 게시물입니다."));
        BlogBoard blogBoard = blogBoardRepository.findById(request.getBlogBoardId()).orElseThrow(()-> new RuntimeException("존제하지 않는 게시판입니."));
        blogPost.update(blogBoard, request.getTitle(),request.getDescription());

        return BlogPostResponse.from(blogPostRepository.save(blogPost));
    }

    public void deleteBlogPost(Long blogPostId) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId).orElseThrow(() -> new RuntimeException("존재하지 않는 게시물입니다."));
        blogPostRepository.delete(blogPost);
    }
}