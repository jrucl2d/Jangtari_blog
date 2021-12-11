package com.yu.jangtari.api.post.domain;

import com.yu.jangtari.common.DateAuditing;
import com.yu.jangtari.common.DeleteFlag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "picture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Picture extends DateAuditing
{

    @Id
    @Column(name = "picutre_url")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    private Picture(String url, Post post) {
        this.url = url;
        this.post = post;
        this.deleteFlag = new DeleteFlag();
    }

    public static List<Picture> getPictureEntities(List<String> urls, Post post) {
        return urls
            .stream()
            .map(url -> Picture.builder().url(url).post(post).build())
            .collect(Collectors.toList());
    }

    public void softDelete() {
        this.deleteFlag.softDelete();
    }
}
