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

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> childComments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Embedded
    private DeleteFlag deleteFlag;

    @Builder
    private Comment(Long id, String content) {
        this.id = id;
        this.content = content;
        this.deleteFlag = new DeleteFlag();
    }

    public void softDelete() {
        this.deleteFlag.softDelete();
    }

    public void initPostAndMember(final Post post, final Member member) {
        this.post = post;
        this.member = member;
        post.addComment(this);
    }
    public void addChildComment(final Comment comment) {
        comment.initParentComment(this);
        this.childComments.add(comment);
    }
    private void initParentComment(final Comment comment) {
        this.parentComment = comment;
    }
    public void updateComment(CommentDto.Update commentDTO) {
        this.content = commentDTO.getContent();
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
