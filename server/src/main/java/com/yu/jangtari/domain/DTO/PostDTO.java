package com.yu.jangtari.domain.DTO;

import com.sun.istack.NotNull;
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
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetOne {
        private Long postId;
        private String title;
        private String content;
        private List<CommentDTO.Get> comments = new ArrayList<>();
        private List<PictureDTO> pictures = new ArrayList<>(); // 후에 사진에 대한 메타 정보가 추가될 것을 고려하여 DTO
        private List<String> hashtags = new ArrayList<>(); // 단순한 해시태그 문자열만 필요하므로 String

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
                    .comments(post.getComments().stream().map(CommentDTO.Get::of).collect(Collectors.toList()))
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
        @NotNull // Long에는 NotBlank 붙일 수 없음
        private Long categoryId;
        @NotBlank(message = "제목이 빈칸이면 안 됩니다.")
        private String title;
        @NotBlank(message = "내용이 빈칸이면 안 됩니다.")
        private String content;
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
        public List<Hashtag> takeHashtagsEntity() {
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
