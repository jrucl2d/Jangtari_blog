package com.yu.jangtari.api.comment.dto;

import com.yu.jangtari.api.comment.domain.Comment;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDto
{
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Get{
        private Long commentId;
        private String content;
        private String username;
        private String nickname;
        private Long parentCommentId;

        @Builder
        private Get(Long commentId, String content, String username, String nickname, Long parentCommentId) {
            this.commentId = commentId;
            this.content = content;
            this.username = username;
            this.nickname = nickname;
            this.parentCommentId = parentCommentId;
        }

        public static List<Get> toList(List<Comment> comments) {
            return comments.stream()
                .map(Get::of)
                .collect(Collectors.toList());
        }

        public static Get of(Comment comment) {
            Long parentId = comment.getParentComment() == null ? null : comment.getParentComment().getId();
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
        @NotNull(message = "postId가 비어있으면 안 됩니다.")
        private Long postId;

        // TODO : AuthUtil 생성한 뒤 commenter 필요 없음
        @NotBlank(message = "작성자가 빈칸이면 안 됩니다.")
        private String commenter;
        @NotBlank(message = "내용이 빈칸이면 안 됩니다.")
        private String content;

        private Long parentCommentId;

        @Builder
        private Add(Long postId, String commenter, String content, Long parentCommentId) {
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
