package com.portalSite.blog.service;

import com.portalSite.blog.dto.request.CreateBlogBoardRequest;
import com.portalSite.blog.dto.request.UpdateBlogBoardRequest;
import com.portalSite.blog.dto.response.BlogBoardResponse;
import com.portalSite.blog.entity.Blog;
import com.portalSite.blog.entity.BlogBoard;
import com.portalSite.blog.repository.BlogBoardRepository;
import com.portalSite.blog.repository.BlogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogBoardService {

    private final BlogBoardRepository blogBoardRepository;
    private final BlogRepository blogRepository;

    public BlogBoardResponse saveBlogBoard(CreateBlogBoardRequest request, Long blogId, Long memberId) {
        Blog blog = blogRepository.findById(blogId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 블로그입니다."));
        if (!blog.getMember().getId().equals(memberId)) {
            throw new RuntimeException("다른 사람의 블로그입니다.");
        }
        BlogBoard blogBoard = BlogBoard.of(blog, request.category());

        return BlogBoardResponse.from(blogBoardRepository.save(blogBoard));
    }

    @Transactional(readOnly = true)
    public List<BlogBoardResponse> getAllBlogBoard(Long blogId) {
        List<BlogBoard> blogBoardList = blogBoardRepository.findAllByBlogId(blogId);

        return blogBoardList.stream().map(BlogBoardResponse::from).toList();
    }

    public BlogBoardResponse updateBlogBoard(UpdateBlogBoardRequest request, Long categoryId, Long memberId) {

        BlogBoard blogBoard = blogBoardRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 게시판입니다."));

        if (!memberId.equals(blogBoard.getBlog().getMember().getId())) {
            throw new RuntimeException("다른 사람의 블로그입니다.");
        }
        blogBoard.update(request.category());

        return BlogBoardResponse.from(blogBoardRepository.save(blogBoard));
    }

    public void deleteBlogBoard(Long categoryId, Long memberId) {

        BlogBoard blogBoard = blogBoardRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 게시판입니다."));

        if (!blogBoard.getBlog().getMember().getId().equals(memberId)) {
            throw new RuntimeException("다른 사람의 블로그입니다.");
        }

        blogBoardRepository.delete(blogBoard);
    }
}
