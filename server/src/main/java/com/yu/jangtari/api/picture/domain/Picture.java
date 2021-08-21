package com.yu.jangtari.api.picture.domain;

import com.yu.jangtari.api.post.domain.Post;
import com.yu.jangtari.common.DateAuditing;
import com.yu.jangtari.common.DeleteFlag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@ToString(exclude = "post")
@Entity
@Table(name = "picture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of="url", callSuper = false)
public class Picture extends DateAuditing
{

    @Id
    @Column(name = "picutre_url")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Embedded
    private DeleteFlag deleteFlag;

    @Builder
    private Picture(String url, Post post) {
        this.url = url;
        this.post = post;
        this.deleteFlag = new DeleteFlag();
    }

    public static Picture of(String url, Post post) {
        return Picture.builder()
            .url(url)
            .post(post)
            .build();
    }

    public void softDelete() {
        this.deleteFlag.softDelete();
    }
}
