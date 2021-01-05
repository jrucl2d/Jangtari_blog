package com.yu.jangtari.domain.DTO;

import lombok.*;

public class CommentDTO {
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Get{
        private Long commentId;
        private String comment;
        private Long memberId;
        private String nickName;
        private String recomment;
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Add{
        private Long postId;
        private String comment;
        private Long recommentId;
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
