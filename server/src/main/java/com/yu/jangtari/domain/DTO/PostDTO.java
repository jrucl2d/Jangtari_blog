package com.yu.jangtari.domain.DTO;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class PostDTO {
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetAll{
        private Long id;
        private String title;
    }
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetOne{
        private Long id;
        private String title;
        // comments
        // pictures
    }
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Add{
        private Long categoryId;
        private String title;
        private String post;
        private int template;
        private List<PictureDTO> pictures = new ArrayList<>();
        private List<HashtagDTO> hashtags = new ArrayList<>();
    }
}
