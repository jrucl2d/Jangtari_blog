package com.yu.jangtari.domain;

import com.yu.jangtari.domain.DTO.CommentDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(exclude = {"post", "parentComment"})
@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of="id", callSuper = false)
public class Comment extends DateAuditing{
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

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<Comment> childComments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Embedded
    private DeleteFlag deleteFlag;

    @Builder
    public Comment(String content) {
        this.content = content;
        this.deleteFlag = DeleteFlag.initDeleteFlag();
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
    public void updateComment(CommentDTO.Update commentDTO) {
        this.content = commentDTO.getContent();
    }
}
