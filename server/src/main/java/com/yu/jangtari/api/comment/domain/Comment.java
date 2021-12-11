package com.yu.jangtari.api.comment.domain;

import com.yu.jangtari.api.comment.dto.CommentDto;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.post.domain.Post;
import com.yu.jangtari.common.DateAuditing;
import com.yu.jangtari.common.DeleteFlag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends DateAuditing
{
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> childComments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    private Comment(Long id, String content, Post post, Member member, Comment parentComment, List<Comment> childComments) {
        this.id = id;
        this.content = content;
        this.post = post;
        this.member = member;
        this.parentComment = parentComment;
        this.childComments = childComments == null ? new ArrayList<>() : childComments;
        this.deleteFlag = new DeleteFlag();
    }

    public void softDelete() {
        this.deleteFlag.softDelete();
    }

    public Comment updateComment(CommentDto.Update commentDto) {
        return Comment.builder()
            .id(this.id)
            .content(commentDto.getContent())
            .member(this.member)
            .parentComment(this.parentComment)
            .post(this.post)
            .childComments(this.childComments)
            .build();
    }
}
