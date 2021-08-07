package com.yu.jangtari.api.picture.domain;

import com.yu.jangtari.common.DateAuditing;
import com.yu.jangtari.common.DeleteFlag;
import com.yu.jangtari.api.post.domain.Post;
import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

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
    public Picture(String url, Post post) {
        this.url = url;
        this.post = post;
        this.deleteFlag = DeleteFlag.initDeleteFlag();
    }

    // TODO : 여기 삭제 필요
    public static List<Picture> stringsToPictures(List<String> pictureURLs, Post post) {
        return pictureURLs.stream().map(url -> Picture.builder().url(url).post(post).build()).collect(Collectors.toList());
    }
}
