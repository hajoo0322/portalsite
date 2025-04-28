package com.portalSite.comment.entity;

import com.portalSite.common.BaseEntity;
import com.portalSite.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", nullable = false)
    private PostType postType;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String content;

    public void updateContent(String content) {
        this.content = content;
    }

    public Comment(Member member, PostType type, Long postId, String content) {
        this.member = member;
        this.postType = type;
        this.postId = postId;
        this.content = content;
    }

    public static Comment of(Member member, PostType type, Long postId, String content) {
        return new Comment(member, type, postId, content);
    }
}
