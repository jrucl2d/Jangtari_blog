package com.yu.jangtari.domain.DTO;

import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.Hashtag;
import com.yu.jangtari.domain.Picture;
import com.yu.jangtari.domain.Post;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PostDTO {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetOne {
        private Long postId;
        private String title;
        private String content;
        private List<CommentDTO.Get> comments = new ArrayList<>();
        private List<PictureDTO> pictures = new ArrayList<>();
        private List<String> hashtags = new ArrayList<>();

        @Builder
        public GetOne(Long postId, String title, String content, List<CommentDTO.Get> comments, List<PictureDTO> pictures, List<String> hashtags) {
            this.postId = postId;
            this.title = title;
            this.content = content;
            this.comments = comments;
            this.pictures = pictures;
            this.hashtags = hashtags;
        }
        public static GetOne of(final Post post) {
            return GetOne.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .comments(post.getComments().stream().map(CommentDTO.Get::new).collect(Collectors.toList()))
                    .pictures(post.getPictures().stream().map(PictureDTO::new).collect(Collectors.toList()))
                    .hashtags(post.getPostHashtags().stream().map(postHashtag -> postHashtag.getHashtag().getContent()).collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Get {
        private Long postId;
        private String title;

        @Builder
        public Get(Long postId, String title) {
            this.postId = postId;
            this.title = title;
        }

        public static Get of(final Post post) {
            return Get.builder().postId(post.getId()).title(post.getTitle()).build();
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Add{
        @NotBlank(message = "카테고리 ID가 빈칸이면 안 됩니다.")
        private Long categoryId;
        @NotBlank(message = "제목이 빈칸이면 안 됩니다.")
        private String title;
        @NotBlank(message = "내용이 빈칸이면 안 됩니다.")
        private String content;
        @NotBlank(message = "템플릿이 빈칸이면 안 됩니다.")
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
        public List<Hashtag> getHashtagsEntity() {
            if (hashtags == null) return Collections.emptyList(); // return EmptyList
            return hashtags.stream().map(Hashtag::new).collect(Collectors.toList());
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Update{
        @NotBlank(message = "제목이 빈칸이면 안 됩니다.")
        private String title;
        @NotBlank(message = "내용이 빈칸이면 안 됩니다.")
        private String content;
        @NotBlank(message = "템플릿이 빈칸이면 안 됩니다.")
        private int template;
        private List<String> hashtags = new ArrayList<>();
        private List<String> delPics = new ArrayList<>();
        private List<MultipartFile> addPics = new ArrayList<>();

        @Builder
        public Update(String title, String content, int template, List<String> hashtags, List<String> delPics, List<MultipartFile> addPics) {
            this.title = title;
            this.content = content;
            this.template = template;
            this.hashtags = hashtags;
            this.delPics = delPics;
            this.addPics = addPics;
        }
        public List<Hashtag> getHashtagsEntity() {
            if (hashtags == null) return Collections.emptyList();
            return hashtags.stream().map(Hashtag::new).collect(Collectors.toList());
        }
        public List<Picture> getDeletePictures() {
            if (delPics == null) return Collections.emptyList();
            return delPics.stream().map(delPic -> Picture.builder().url(delPic).build()).collect(Collectors.toList());
        }
    }
}
