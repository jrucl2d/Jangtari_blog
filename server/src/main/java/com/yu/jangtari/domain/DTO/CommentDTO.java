package com.yu.jangtari.domain.DTO;

import lombok.*;

public class CommentDTO {
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Add{
        private Long postId;
        private String comment;
        private Long recomment;
    }
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update{
        private Long id;
        private String comment;
    }
}
