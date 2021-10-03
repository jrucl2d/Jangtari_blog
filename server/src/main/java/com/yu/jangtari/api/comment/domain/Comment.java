package com.yu.jangtari.api.comment.domain;

import com.yu.jangtari.api.comment.dto.CommentDto;
import com.yu.jangtari.common.DateAuditing;
import com.yu.jangtari.common.DeleteFlag;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.post.domain.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@ToString(exclude = {"post", "parentComment"})
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id.equals(comment.id)
            && Objects.equals(content, comment.content)
            && Objects.equals(member, comment.member)
            && Objects.equals(parentComment, comment.parentComment)
            && Objects.equals(childComments, comment.childComments)
            && Objects.equals(post, comment.post)
            && Objects.equals(deleteFlag, comment.deleteFlag);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, content, member, parentComment, childComments, post, deleteFlag);
    }
}
