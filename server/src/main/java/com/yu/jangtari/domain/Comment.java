package com.yu.jangtari.domain;

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
    @JoinColumn(name="recomment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "recomment", cascade = CascadeType.ALL)
    private List<Comment> subcomments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Embedded
    private DeleteFlag deleteFlag;

    @Builder
    public Comment(String content, Member member, Post post) {

    }
}
