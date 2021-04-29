package com.yu.jangtari.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    @Embedded
    private DeleteFlag deleteFlag;
}
