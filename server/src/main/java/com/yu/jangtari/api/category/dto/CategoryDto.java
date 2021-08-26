package com.yu.jangtari.api.category.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.util.GoogleDriveUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryDto
{

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Add {
        @NotBlank(message = "이름이 빈칸이면 안 됩니다.")
        private String name;

        @Builder
        public Add(String name) {
            this.name = name;
        }

        public Category toEntity(GoogleDriveUtil googleDriveUtil, MultipartFile picture) {
            String returnedUrl = googleDriveUtil.fileToURL(picture, GDFolder.CATEGORY);
            return Category.builder()
                    .name(name)
                    .picture(returnedUrl)
                    .build();
        }
    }
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString
    public static class Update {
        @NotNull
        private Long categoryId;

        @NotBlank(message = "이름이 빈칸이면 안 됩니다.")
        private String name;

        @JsonIgnore
        private String pictureUrl;

        @Builder
        public Update(Long categoryId, String name, String pictureUrl) {
            this.categoryId = categoryId;
            this.name = name;
            this.pictureUrl = pictureUrl;
        }

        @JsonIgnore
        public String getPictureUrl() {
            return this.pictureUrl;
        }

        public Update toUrlDto(GoogleDriveUtil googleDriveUtil, MultipartFile picture) {
            String returnedUrl = googleDriveUtil.fileToURL(picture, GDFolder.CATEGORY);
            return Update.builder()
                .name(this.name)
                .pictureUrl(returnedUrl)
                .build();
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
        public static CategoryDto.Get of(Category category) {
            return Get.builder().id(category.getId()).name(category.getName()).picture(category.getPicture()).build();
        }
    }
}
