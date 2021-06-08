package com.yu.jangtari.domain.DTO;

import com.yu.jangtari.domain.Category;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

public class CategoryDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Add{
        @NotBlank(message = "이름이 빈칸이면 안 됩니다.")
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
    }
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString
    public static class Update{
        @NotBlank(message = "이름이 빈칸이면 안 됩니다.")
        private String name;
        private MultipartFile picture;

        @Builder
        public Update(String name, MultipartFile picture) {
            this.name = name;
            this.picture = picture;
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
