package com.yu.jangtari.domain;

import com.yu.jangtari.domain.DTO.PostDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @Embedded
    DeleteFlag deleteFlag;

    @Builder
    public Post(String title, String content, int template, Category category) {
        this.title = title;
        this.content = content;
        this.template = template;
        this.category = category;
        this.deleteFlag = DeleteFlag.initDeleteFlag();
    }
    public void initPictures(List<String> pictures) {
        this.pictures = pictures.stream().map(url -> Picture.builder().post(this).url(url).build()).collect(Collectors.toList());
    }
    public void initPostHashtags(List<Hashtag> hashtags) {
        this.postHashtags = hashtags.stream().map(hashtag ->
                PostHashtag.builder()
                        .post(this)
                        .hashtag(hashtag)
                        .build()).collect(Collectors.toList());
    }

    public void addComment(final Comment comment) {
        this.getComments().add(comment);
    }

    public void updateTitleContentTemplate(PostDTO.Update postDTO) {
        this.title = postDTO.getTitle();
        this.content = postDTO.getContent();
        this.template = postDTO.getTemplate();
    }
}
