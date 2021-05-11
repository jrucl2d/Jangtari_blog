package com.yu.jangtari.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="post_hashtag")
@EqualsAndHashCode(of="id", callSuper = false)
public class PostHashtag extends DateAuditing{
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

    @Embedded
    private DeleteFlag deleteFlag;

    @Builder
    public PostHashtag(Post post, Hashtag hashtag) {
        this.post = post;
        this.hashtag =hashtag;
        this.deleteFlag = DeleteFlag.initDeleteFlag();
    }
}
