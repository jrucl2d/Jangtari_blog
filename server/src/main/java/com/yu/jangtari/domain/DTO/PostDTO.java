package com.yu.jangtari.domain.DTO;

import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.Hashtag;
import com.yu.jangtari.domain.Post;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PostDTO {
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetAll{
        private Long id;
        private String title;
        private int template;
    }
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetOne{
        private Long id;
        private String title;
        private String content;
        private List<CommentDTO.Get> comments = new ArrayList<>();
        private List<PictureDTO> pictures = new ArrayList<>();
        private List<String> hashtags = new ArrayList<>();

        @Builder
        public GetOne(Long id, String title, String content, List<CommentDTO.Get> comments, List<PictureDTO> pictures, List<String> hashtags) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.comments = comments;
            this.pictures = pictures;
            this.hashtags = hashtags;
        }
        public static GetOne of(final Post post) {
            return GetOne.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .comments(null) // 여기 변경 필요
                    .pictures(post.getPictures().stream().map(picture ->
                        PictureDTO.builder().picture(picture.getUrl()).build()
                    ).collect(Collectors.toList()))
                    .hashtags(post.getPostHashtags().stream().map(postHashtag -> postHashtag.getHashtag().getContent()).collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Add{
        @NotEmpty
        private Long categoryId;
        @NotEmpty
        private String title;
        @NotEmpty
        private String content;
        @NotEmpty
        private int template;
        private List<String> hashtags = new ArrayList<>();
        private List<MultipartFile> pictures = new ArrayList<>();

        @Builder
        public Add(Long categoryId, String title, String content, int template, List<String> hashtags, List<MultipartFile> pictures) {
            this.categoryId = categoryId;
            this.title = title;
            this.content = content;
            this.hashtags = hashtags;
            this.template = template;
            this.hashtags = hashtags;
            this.pictures = pictures;
        }

        public Post toEntity(final Category category) {
            return Post.builder()
                    .category(category)
                    .title(title)
                    .content(content)
                    .template(template)
                    .build();
        }
        public List<Hashtag> getHashtags() {
            if (hashtags == null) return Arrays.asList(); // return EmptyList
            return hashtags.stream().map(hashtagString -> new Hashtag(hashtagString)).collect(Collectors.toList());
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Update{
        @NotEmpty
        private Long postId;
        @NotEmpty
        private String title;
        @NotEmpty
        private String content;
        @NotEmpty
        private int template;
        private List<String> hashtags = new ArrayList<>();
        private List<String> delPics = new ArrayList<>();
        private List<MultipartFile> addPics = new ArrayList<>();

        @Builder
        public Update(Long postId, String title, String content, int template, List<String> hashtags, List<String> delPics, List<MultipartFile> addPics) {
            this.postId = postId;
            this.title = title;
            this.content = content;
            this.template = template;
            this.hashtags = hashtags;
            this.delPics = delPics;
            this.addPics = addPics;
        }
        public List<Hashtag> getHashtags() {
            if (hashtags == null) return Arrays.asList();
            return hashtags.stream().map(hashtagString -> new Hashtag(hashtagString)).collect(Collectors.toList());
        }
    }
}
