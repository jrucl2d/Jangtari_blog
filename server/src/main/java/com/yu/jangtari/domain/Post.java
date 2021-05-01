package com.yu.jangtari.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(exclude = "category")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
@EqualsAndHashCode(of="id", callSuper = false)
public class Post extends DateAuditing {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int template;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Picture> pictures = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @Embedded
    DeleteFlag deleteFlag;

    @Builder
    public Post(String title, String content, int template, List<Picture> pictures, Category category, List<PostHashtag> posthashtags) {
        this.title = title;
        this.content = content;
        this.template = template;
        this.pictures = pictures;
        this.category = category;
        this.postHashtags = posthashtags;
    }
}
