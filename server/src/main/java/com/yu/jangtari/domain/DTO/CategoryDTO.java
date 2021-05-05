package com.yu.jangtari.domain.DTO;

import com.yu.jangtari.domain.Category;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CategoryDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Add{
        @NotEmpty
        @NotNull
        private String name;
        private MultipartFile picture;

        @Builder
        public Add(String name, MultipartFile picture) {
            this.name = name;
            this.picture = picture;
        }
        public Category toEntity(String pictureURL) {
            return Category.builder()
                    .name(name)
                    .picture(pictureURL)
                    .build();
        }
        public void addPictureIfExists(MultipartFile pictureFile) {
            if (pictureFile != null) this.picture = pictureFile;
        }
    }
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Update{
        @NotEmpty
        @NotNull
        private String name;
        private MultipartFile picture;

        @Builder
        public Update(String name, MultipartFile picture) {
            this.name = name;
            this.picture = picture;
        }
        public void addPictureIfExists(MultipartFile pictureFile) {
            if (pictureFile != null) this.picture = pictureFile;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Get{
        private Long id;
        private String name;
        private String picture;

        @Builder
        private Get(Long id, String name, String picture) {
            this.id = id;
            this.name = name;
            this.picture = picture;
        }
        public static CategoryDTO.Get of(Category category) {
            return Get.builder().id(category.getId()).name(category.getName()).picture(category.getPicture()).build();
        }
    }
}
