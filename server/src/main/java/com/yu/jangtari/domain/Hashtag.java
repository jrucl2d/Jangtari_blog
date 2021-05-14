package com.yu.jangtari.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(exclude = "postHashtags")
@Entity
@Table(name = "hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of="content", callSuper = false)
public class Hashtag extends DateAuditing {
    @Id
    @Column(name = "hashtag")
    private String content;

    @OneToMany(mappedBy = "hashtag")
    private List<PostHashtag> postHashtags = new ArrayList<>();

    public Hashtag(String content) {
        this.content = content;
    }
}
