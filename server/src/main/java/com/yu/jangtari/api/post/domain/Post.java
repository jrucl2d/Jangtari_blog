package com.yu.jangtari.api.post.domain;

import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.comment.domain.Comment;
import com.yu.jangtari.api.post.dto.PostDto;
import com.yu.jangtari.api.post.repository.hashtag.HashtagRepository;
import com.yu.jangtari.common.DateAuditing;
import com.yu.jangtari.common.DeleteFlag;
import lombok.AccessLevel;
import lombok.Builder;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@ToString(exclude = "category")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Picture> pictures = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @Embedded
    DeleteFlag deleteFlag;

    @Builder
    private Post(
        Long id
        , String title
        , String content
        , int template
        , Category category
        , List<Comment> comments
        , List<Picture> pictures
        , List<PostHashtag> postHashtags)
    {
        this.id = id;
        this.title = title;
        this.content = content;
        this.template = template;
        this.category = category;
        this.deleteFlag = new DeleteFlag();
        this.comments = comments == null ? new ArrayList<>() : comments;
        this.pictures = pictures == null ? new ArrayList<>() : pictures;
        this.postHashtags = postHashtags == null ? new ArrayList<>() : postHashtags;
    }

    public static Post of(PostDto.Add dto, HashtagRepository hashtagRepository) {
        Post post = Post.builder()
            .category(Category.builder()
                .id(dto.getCategoryId())
                .build())
            .title(dto.getTitle())
            .content(dto.getContent())
            .template(dto.getTemplate())
            .build();
        post.pictures.addAll(Picture.getPictureEntities(dto.getPictureUrls(), post));
        post.postHashtags.addAll(
            dto.getHashtags()
                .stream()
                .map(hashtagStr -> PostHashtag.of(hashtagRepository.save(new Hashtag(hashtagStr)), post))
                .collect(Collectors.toList())
        );
        return post;
    }

    public static Post of(Post gotPost, List<Picture> pictures, List<PostHashtag> postHashtags, List<Comment> comments) {
        return Post.builder()
                .id(gotPost.getId())
                .title(gotPost.getTitle())
                .content(gotPost.getContent())
                .template(gotPost.getTemplate())
                .category(gotPost.getCategory())
                .comments(comments)
                .pictures(pictures)
                .postHashtags(postHashtags)
                .build();
    }

    public Post updatePost(PostDto.Update dto, HashtagRepository hashtagRepository) {
        List<PostHashtag> newPostHashtags = dto.getHashtags()
            .stream()
            .map(hashtagStr ->
                PostHashtag.of(hashtagRepository.save(new Hashtag(hashtagStr)), this))
            .collect(Collectors.toList());

        List<Picture> newPictures = this.pictures
            .stream()
            .filter(picture -> !dto.getDelPics().contains(picture.getUrl()))
            .collect(Collectors.toList());

        newPictures.addAll(Picture.getPictureEntities(dto.getAddPicUrls(), this));

        return Post.builder()
            .id(this.id)
            .title(dto.getTitle())
            .content(dto.getContent())
            .template(dto.getTemplate())
            .pictures(newPictures)
            .postHashtags(newPostHashtags)
            .build();
    }

    public void softDelete() {
        this.deleteFlag.softDelete();
        this.comments.forEach(Comment::softDelete);
        this.pictures.forEach(Picture::softDelete);
        this.postHashtags.forEach(PostHashtag::softDelete);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return template == post.template
            && id.equals(post.id)
            && Objects.equals(title, post.title)
            && Objects.equals(content, post.content)
            && Objects.equals(comments, post.comments)
            && Objects.equals(pictures, post.pictures)
            && Objects.equals(category, post.category)
            && Objects.equals(postHashtags, post.postHashtags)
            && Objects.equals(deleteFlag, post.deleteFlag);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, title, content, template, comments, pictures, category, postHashtags, deleteFlag);
    }
}
