package com.yu.jangtari.api.post.domain;

import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.comment.domain.Comment;
import com.yu.jangtari.api.post.dto.PostDTO;
import com.yu.jangtari.common.DateAuditing;
import com.yu.jangtari.common.DeleteFlag;
import com.yu.jangtari.api.picture.domain.Picture;
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
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Picture> pictures;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHashtag> postHashtags;

    @Embedded
    DeleteFlag deleteFlag;

    @Builder
    public Post(String title, String content, int template, Category category) {
        this.title = title;
        this.content = content;
        this.template = template;
        this.category = category;
        this.deleteFlag = new DeleteFlag();
        this.comments = new ArrayList<>();
        this.pictures = new ArrayList<>();
        this.postHashtags = new ArrayList<>();
    }

    public void softDelete() {
        this.deleteFlag.softDelete();
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
    public void removePictures(PostDTO.Update postDTO) {
        getPictures().removeAll(postDTO.getDeletePictures());
    }
    public void addComment(final Comment comment) {
        this.getComments().add(comment);
    }
    public void updatePost(PostDTO.Update postDTO) {
        this.title = postDTO.getTitle();
        this.content = postDTO.getContent();
        this.template = postDTO.getTemplate();
    }
    public Post getDeleteFiltered() {
        final Post resultPost = Post.builder()
                .title(this.getTitle())
                .category(this.getCategory())
                .template(this.getTemplate())
                .content(this.getContent()).build();
        this.getComments().forEach(comment -> {
            if (!comment.getDeleteFlag().isDeleted()) resultPost.addComment(comment);
        });
        resultPost.addPictures(this.getPictures().stream().filter(picture -> !picture.getDeleteFlag().isDeleted()).collect(Collectors.toList()));
        resultPost.addPostHashtags(this.getPostHashtags().stream().filter(postHashtag -> !postHashtag.getDeleteFlag().isDeleted()).collect(Collectors.toList()));
        resultPost.setId(this.getId());
        return resultPost;
    }
    // getDeleteFiltered를 위한 setter
    private void setId(Long id) {
        this.id = id;
    }
}
