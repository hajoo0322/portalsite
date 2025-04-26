package com.portalSite.blog.service;

import com.portalSite.blog.dto.request.CreateBlogRequest;
import com.portalSite.blog.dto.request.UpdateBlogRequest;
import com.portalSite.blog.dto.response.BlogResponse;
import com.portalSite.blog.entity.Blog;
import com.portalSite.blog.repository.BlogRepository;
import com.portalSite.member.entity.Member;
import com.portalSite.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final MemberRepository memberRepository;

    public BlogResponse saveBlog(CreateBlogRequest request, Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(()->new RuntimeException("존재하지 않는 유저입니다."));
        Blog blog = Blog.of(member, request.getName(),request.getDescription());

         return BlogResponse.from(blogRepository.save(blog));
    }

    public List<BlogResponse> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();

        return blogs.stream().map(BlogResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public BlogResponse getBlog(Long blogId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new RuntimeException("존재하지 않는 블로그입니다."));
        return BlogResponse.from(blog);
    }

    public BlogResponse updateBlog(UpdateBlogRequest request, Long blogId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new RuntimeException("존재하지 않는 블로그입니다."));
        blog.update(request.getName(), request.getDescription());
        return BlogResponse.from(blog);
    }

    public void deleteBlog(Long blogId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new RuntimeException("존재하지 않는 블로그입니다."));
        blogRepository.delete(blog);
    }
}
