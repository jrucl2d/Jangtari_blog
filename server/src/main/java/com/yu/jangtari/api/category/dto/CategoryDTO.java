package com.yu.jangtari.api.category.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yu.jangtari.api.category.domain.Category;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Add {
        @NotBlank(message = "이름이 빈칸이면 안 됩니다.")
        private String name;
        private MultipartFile picture;

        @JsonIgnore
        private String pictureURL;

        @Builder
        public Add(String name, MultipartFile picture) {
            this.name = name;
            this.picture = picture;
        }
        public Category toEntity() {
            return Category.builder()
                    .name(name)
                    .picture(pictureURL)
                    .build();
        }

        public void setPictureURL(String pictureURL) {
            this.picture = null;
            this.pictureURL = pictureURL;
        }
    }
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString
    public static class Update {
        @NotBlank(message = "이름이 빈칸이면 안 됩니다.")
        private String name;
        private MultipartFile picture;

        @JsonIgnore
        private String pictureURL;

        @Builder
        public Update(String name, MultipartFile picture) {
            this.name = name;
            this.picture = picture;
        }

        @JsonIgnore
        public String getPictureURL() {
            return this.pictureURL;
        }

        public void setPictureURL(String pictureURL) {
            this.picture = null;
            this.pictureURL = pictureURL;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Get{
        private Long categoryId;
        private String name;
        private String picture;

        @Builder
        private Get(Long id, String name, String picture) {
            this.categoryId = id;
            this.name = name;
            this.picture = picture;
        }
        public static CategoryDTO.Get of(Category category) {
            return Get.builder().id(category.getId()).name(category.getName()).picture(category.getPicture()).build();
        }
    }
}
