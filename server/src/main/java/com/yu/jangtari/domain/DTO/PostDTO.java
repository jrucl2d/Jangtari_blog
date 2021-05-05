package com.yu.jangtari.domain.DTO;

import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.Hashtag;
import com.yu.jangtari.domain.Post;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
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
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetOne{
        private Long id;
        private String title;
        private String content;
        private List<CommentDTO.Get> comments = new ArrayList<>();
        private List<PictureDTO> pictures = new ArrayList<>();
        private List<String> hashtags = new ArrayList<>();
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

        public Post toEntity(Category category) {
            return Post.builder()
                    .category(category)
                    .title(title)
                    .content(content)
                    .template(template)
                    .build();
        }
        public List<Hashtag> getHashtags() {
            return hashtags.stream().map(hashtagString -> new Hashtag(hashtagString)).collect(Collectors.toList());
        }
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update{
        private Long id;
        private String title;
        private String post;
        private int template;
        private List<String> hashtags = new ArrayList<>();
        private List<Long> delPics = new ArrayList<>();
    }
}
