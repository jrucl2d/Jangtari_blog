package com.yu.jangtari.domain.DTO;

import com.yu.jangtari.domain.Comment;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.domain.Post;
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
        private String username;
        private String nickname;
        private Long recomment;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Add{
        private Long postId;
        private String commenter;
        private String content;
        private Long parentCommentId;

        public Comment toEntity() {
            return Comment.builder()
                    .content(content)
                    .build();
        }
    }
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update{
        private Long id;
        private String commenter;
        private String comment;
    }
}
