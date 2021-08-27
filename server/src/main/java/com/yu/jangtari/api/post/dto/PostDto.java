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
        private int template;
        private List<String> hashtags = new ArrayList<>();
        private List<MultipartFile> pictures = new ArrayList<>();

        @JsonIgnore
        private List<String> pictureUrls = new ArrayList<>();

        @Builder
        private Add(
            Long categoryId
            , String title
            , String content
            , int template
            , List<String> hashtags
            , List<MultipartFile> pictures
            , List<String> pictureUrls) {
            this.categoryId = categoryId;
            this.title = title;
            this.content = content;
            this.template = template;
            if (pictures == null) this.pictures = new ArrayList<>();
            else this.pictures = pictures;
            if (pictureUrls == null) this.pictureUrls = new ArrayList<>();
            else this.pictureUrls = pictureUrls;
            if (hashtags == null) this.hashtags = new ArrayList<>();
            else this.hashtags = hashtags;
        }

        public Add toUrlDto(GoogleDriveUtil googleDriveUtil) {
            List<String> retUrls = googleDriveUtil.filesToURLs(this.pictures, GDFolder.POST);
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
        @NotBlank(message = "제목이 빈칸이면 안 됩니다.")
        private String title;
        @NotBlank(message = "내용이 빈칸이면 안 됩니다.")
        private String content;
        private int template;
        private List<String> hashtags = new ArrayList<>();
        private List<String> delPics = new ArrayList<>();
        private List<MultipartFile> addPics = new ArrayList<>();

        @JsonIgnore
        private List<String> addPicUrls = new ArrayList<>();

        @Builder
        private Update(String title
            , String content
            , int template
            , List<String> hashtags
            , List<String> delPics
            , List<MultipartFile> addPics
            , List<String> addPicUrls) {
            this.title = title;
            this.content = content;
            this.template = template;
            if (hashtags == null) this.hashtags = new ArrayList<>();
            else this.hashtags = hashtags;
            if (delPics == null) this.delPics = new ArrayList<>();
            else this.delPics = delPics;
            if (addPics == null) this.addPics = new ArrayList<>();
            else this.addPics = addPics;
            if (addPicUrls == null) this.addPicUrls = new ArrayList<>();
            else this.addPicUrls = addPicUrls;
        }

        public Update toUrlDto(GoogleDriveUtil googleDriveUtil) {
            List<String> retUrls = googleDriveUtil.filesToURLs(this.addPics, GDFolder.POST);
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
