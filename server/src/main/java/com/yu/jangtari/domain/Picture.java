package com.yu.jangtari.domain;

import lombok.*;
import javax.persistence.*;

@Getter
@ToString(exclude = "post")
@Entity
@Table(name = "picture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of="url", callSuper = false)
public class Picture extends DateAuditing {

    @Id
    @Column(name = "picutre_url")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Embedded
    private DeleteFlag deleteFlag;

    @Builder
    public Picture(String url, Post post) {
        this.url = url;
        this.post = post;
        this.deleteFlag = DeleteFlag.initDeleteFlag();
    }
}
