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
}
