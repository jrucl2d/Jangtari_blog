package com.yu.jangtari.api.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yu.jangtari.api.comment.dto.CommentDto;
import com.yu.jangtari.api.post.domain.Post;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto
{
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetOne {
        private Long postId;
        private String title;
        private String content;
        private List<CommentDto.Get> comments = new ArrayList<>();
        private List<PictureDto> pictures = new ArrayList<>(); // 후에 사진에 대한 메타 정보가 추가될 것을 고려하여 DTO
        private List<String> hashtags = new ArrayList<>(); // 단순한 해시태그 문자열만 필요하므로 String

        @Builder
        private GetOne(Long postId, String title, String content, List<CommentDto.Get> comments, List<PictureDto> pictures, List<String> hashtags) {
            this.postId = postId;
            this.title = title;
            this.content = content;
            this.comments = comments;
            this.pictures = pictures;
            this.hashtags = hashtags;
        }
        public static GetOne of(Post post) {
            return GetOne.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .comments(post.getComments().stream().map(CommentDto.Get::of).collect(Collectors.toList()))
                    .pictures(post.getPictures().stream().map(PictureDto::new).collect(Collectors.toList()))
                    .hashtags(post.getPostHashtags().stream().map(postHashtag -> postHashtag.getHashtag().getContent()).collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ListGetElement {
        private Long postId;
        private String title;

        @Builder
        private ListGetElement(Long postId, String title) {
            this.postId = postId;
            this.title = title;
        }

        public static ListGetElement of(Post post) {
            return ListGetElement.builder().postId(post.getId()).title(post.getTitle()).build();
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Add{
        @NotNull(message = "categoryId는 null 이면 안 됩니다.")
        private Long categoryId;

        @NotBlank(message = "제목이 빈칸이면 안 됩니다.")
        private String title;

        @NotBlank(message = "내용이 빈칸이면 안 됩니다.")
        private String content;

        private Integer template;

        private List<String> hashtags = new ArrayList<>();

        @JsonIgnore
        private List<String> pictureUrls;

        @Builder
        public Add(
            Long categoryId
            , String title
            , String content
            , Integer template
            , List<String> hashtags
            , List<String> pictureUrls)
        {
            this.categoryId = categoryId;
            this.title = title;
            this.content = content;
            this.template = template == null ? 0 : template;
            this.pictureUrls = pictureUrls == null ? new ArrayList<>() : pictureUrls;
            this.hashtags = hashtags == null ? new ArrayList<>() : hashtags;
        }

        public Add toUrlDto(GoogleDriveUtil googleDriveUtil, List<MultipartFile> pictures) {
            List<String> retUrls = googleDriveUtil.filesToURLs(pictures, GDFolder.POST);
            return Add.builder()
                .categoryId(this.categoryId)
                .title(this.title)
                .content(this.content)
                .template(this.template)
                .pictureUrls(retUrls)
                .hashtags(this.hashtags)
                .build();
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Update{
        @NotNull(message = "변경할 게시글 아이디가 비어있으면 안 됩니다.")
        private Long postId;

        @NotBlank(message = "제목이 빈칸이면 안 됩니다.")
        private String title;

        @NotBlank(message = "내용이 빈칸이면 안 됩니다.")
        private String content;

        private int template;

        private List<String> hashtags = new ArrayList<>();

        private List<String> delPics = new ArrayList<>();

        @JsonIgnore
        private List<String> addPicUrls = new ArrayList<>();

        @Builder
        public Update(
            Long postId
            , String title
            , String content
            , int template
            , List<String> hashtags
            , List<String> delPics
            , List<String> addPicUrls)
        {
            this.postId = postId;
            this.title = title;
            this.content = content;
            this.template = template;
            this.hashtags = hashtags == null ? new ArrayList<>() : hashtags;
            this.delPics = delPics == null ? new ArrayList<>() : delPics;
            this.addPicUrls = addPicUrls == null ? new ArrayList<>() : addPicUrls;

        }

        public Update toUrlDto(GoogleDriveUtil googleDriveUtil, List<MultipartFile> pictures) {
            List<String> retUrls = googleDriveUtil.filesToURLs(pictures, GDFolder.POST);
            return Update.builder()
                .title(this.title)
                .content(this.content)
                .template(this.template)
                .hashtags(this.hashtags)
                .delPics(this.delPics)
                .addPicUrls(retUrls)
                .build();
        }
    }
}
