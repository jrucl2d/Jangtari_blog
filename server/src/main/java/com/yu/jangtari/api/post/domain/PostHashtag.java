package com.yu.jangtari.api.post.domain;

import com.yu.jangtari.common.DateAuditing;
import com.yu.jangtari.common.DeleteFlag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="post_hashtag")
public class PostHashtag extends DateAuditing
{
    @Id
    @Column(name = "post_hashtag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    @Builder
    private PostHashtag(Long id, Post post, Hashtag hashtag) {
        this.id = id;
        this.post = post;
        this.hashtag = hashtag;
        this.deleteFlag = new DeleteFlag();
    }

    public static PostHashtag of(Hashtag hashtag, Post post) {
        return PostHashtag.builder()
            .hashtag(hashtag)
            .post(post)
            .build();
    }

    public void softDelete() {
        this.deleteFlag.softDelete();
    }
}
