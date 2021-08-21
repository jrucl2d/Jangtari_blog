package com.yu.jangtari.api.post.domain;

import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.comment.domain.Comment;
import com.yu.jangtari.api.picture.domain.Picture;
import com.yu.jangtari.api.post.dto.PostDto;
import com.yu.jangtari.common.DateAuditing;
import com.yu.jangtari.common.DeleteFlag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString(exclude = "category")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
@EqualsAndHashCode(of="id", callSuper = false)
public class Post extends DateAuditing
{

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Picture> pictures = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post")
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @Embedded
    DeleteFlag deleteFlag;

    @Builder
    private Post(String title, String content, int template, Category category) {
        this.title = title;
        this.content = content;
        this.template = template;
        this.category = category;
        this.deleteFlag = new DeleteFlag();
        this.comments = new ArrayList<>();
        this.pictures = new ArrayList<>();
        this.postHashtags = new ArrayList<>();
    }

    public static Post of(PostDto.Add dto) {
        Post post = Post.builder()
            .category(Category.builder().id(dto.getCategoryId()).build())
            .title(dto.getTitle())
            .content(dto.getContent())
            .template(dto.getTemplate())
            .build();
        post.pictures.addAll(
            dto.getPictureUrls()
                .stream()
                .map(url -> Picture.of(url, post))
                .collect(Collectors.toList())
        );
        post.postHashtags.addAll(
            dto.getHashtags()
                .stream()
                .map(hashtag -> PostHashtag.of(hashtag, post))
                .collect(Collectors.toList())
        );
        return post;
    }

    public void softDelete() {
        this.deleteFlag.softDelete();
        this.comments.forEach(Comment::softDelete);
        this.pictures.forEach(Picture::softDelete);
        this.postHashtags.forEach(PostHashtag::softDelete);
    }

    public void addPictures(List<Picture> pictures) {
        getPictures().addAll(pictures);
    }
    public void addPostHashtags(List<PostHashtag> postHashtags) {
        getPostHashtags().addAll(postHashtags);
    }
    public void clearPostHashtags() {
        this.postHashtags.clear();
    }
    public void removePictures(PostDto.Update postDTO) {
        getPictures().removeAll(postDTO.getDeletePictures());
    }
    public void addComment(final Comment comment) {
        this.getComments().add(comment);
    }
    public void updatePost(PostDto.Update postDTO) {
        this.title = postDTO.getTitle();
        this.content = postDTO.getContent();
        this.template = postDTO.getTemplate();
    }
}
