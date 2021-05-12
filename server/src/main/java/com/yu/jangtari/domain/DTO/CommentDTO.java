package com.yu.jangtari.domain.DTO;

import com.yu.jangtari.domain.Comment;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.domain.Post;
import lombok.*;

import javax.validation.constraints.NotBlank;

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
        @NotBlank(message = "postId가 빈칸이면 안 됩니다.")
        private Long postId;
        @NotBlank(message = "작성자가 빈칸이면 안 됩니다.")
        private String commenter;
        @NotBlank(message = "내용이 빈칸이면 안 됩니다.")
        private String content;
        private Long parentCommentId;

        @Builder
        public Add(Long postId, String commenter, String content, Long parentCommentId) {
            this.postId = postId;
            this.commenter = commenter;
            this.content = content;
            this.parentCommentId = parentCommentId;
        }

        public Comment toEntity() {
            return Comment.builder()
                    .content(content)
                    .build();
        }
    }
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Update{
        private Long id;
        private String commenter;
        private String comment;
    }
}
