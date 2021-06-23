package com.yu.jangtari.domain.DTO;

import com.yu.jangtari.domain.Comment;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Get{
        private Long commentId;
        private String content;
        private String username;
        private String nickname;
        private Long parentCommentId;

        @Builder
        public Get(Long commentId, String content, String username, String nickname, Long parentCommentId) {
            this.commentId = commentId;
            this.content = content;
            this.username = username;
            this.nickname = nickname;
            this.parentCommentId = parentCommentId;
        }
        public static CommentDTO.Get of(Comment comment) {
            final Long parentId = comment.getParentComment() == null ? null : comment.getParentComment().getId();
            return Get.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .username(comment.getMember().getUsername())
                    .nickname(comment.getMember().getNickname())
                    .parentCommentId(parentId)
                    .build();
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Add{
        @NotNull
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
        @NotBlank(message = "작성자가 빈칸이면 안 됩니다.")
        private String commenter;
        @NotBlank(message = "내용이 빈칸이면 안 됩니다.")
        private String content;

        @Builder
        public Update(String commenter, String content) {
            this.commenter = commenter;
            this.content = content;
        }
    }
}
