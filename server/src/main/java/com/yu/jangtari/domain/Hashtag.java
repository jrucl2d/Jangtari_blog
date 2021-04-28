package com.yu.jangtari.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(exclude = "posts")
@Entity
@Table(name = "hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of="id")
public class Hashtag extends DateAuditing{
    @Id
    @Column(name = "hashtag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "hashtag")
    private List<PostHashtag> postHashtags = new ArrayList<>();
}
