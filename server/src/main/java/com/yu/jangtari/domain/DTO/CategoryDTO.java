package com.yu.jangtari.domain.DTO;

import com.yu.jangtari.domain.Category;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class CategoryDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Add{
        @NotEmpty
        private String name;
        private List<MultipartFile> picture = new ArrayList<>();

        @Builder
        public Add(String name, List<MultipartFile> multipartFiles) {
            this.name = name;
            this.picture = multipartFiles;
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
    public static class Update{
        @NotEmpty
        private Long id;
        @NotEmpty
        private String name;
        private List<MultipartFile> picture = new ArrayList<>();

        @Builder
        public Update(Long id, String name, List<MultipartFile> picture) {
            this.id = id;
            this.name = name;
            this.picture = picture;
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
