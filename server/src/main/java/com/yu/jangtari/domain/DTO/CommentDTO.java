package com.yu.jangtari.domain.DTO;

import com.yu.jangtari.domain.Comment;
import lombok.*;

import javax.validation.constraints.NotBlank;

public class CommentDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static class Get{
        private Long commentId;
        private String content;
        private String username;
        private String nickname;
        private Long parentComment;

        @Builder
        public Get(Long commentId, String content, String username, String nickname, Long parentComment) {
            this.commentId = commentId;
            this.content = content;
            this.username = username;
            this.nickname = nickname;
            this.parentComment = parentComment;
        }
        public static CommentDTO.Get of(Comment comment) {
            return Get.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .username(comment.getMember().getUsername())
                    .nickname(comment.getMember().getNickname())
                    .parentComment(comment.getParentComment().getId())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Add{
        @NotBlank(message = "ID가 빈칸이면 안 됩니다.")
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
        private String commenter;
        private String content;

        @Builder
        public Update(String commenter, String content) {
            this.commenter = commenter;
            this.content = content;
        }
    }
}
