package com.yu.jangtari.domain.DTO;

import com.yu.jangtari.domain.Category;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class CategoryDTO {

    @Getter
    @NoArgsConstructor
    public static class Add{
        @Valid
        private String name;
        private List<MultipartFile> picture = new ArrayList<>();

        @Builder
        public Add(@Valid String name, List<MultipartFile> multipartFiles) {
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
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Get{
        private Long id;
        private String name;
        private String picture;
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update{
        private Long id;
        private String name;
    }
}
