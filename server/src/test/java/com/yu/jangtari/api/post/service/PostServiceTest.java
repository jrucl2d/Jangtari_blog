package com.yu.jangtari.api.post.service;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.api.comment.domain.Comment;
import com.yu.jangtari.api.post.domain.Hashtag;
import com.yu.jangtari.api.post.domain.Picture;
import com.yu.jangtari.api.post.domain.Post;
import com.yu.jangtari.api.post.domain.PostHashtag;
import com.yu.jangtari.api.post.dto.PostDto;
import com.yu.jangtari.api.post.repository.hashtag.HashtagRepository;
import com.yu.jangtari.api.post.repository.post.PostRepository;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.testHelper.PictureFileUtil;
import com.yu.jangtari.util.GoogleDriveUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class PostServiceTest extends ServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private HashtagRepository hashtagRepository;

    @Mock
    private GoogleDriveUtil googleDriveUtil;

    @Test
    @DisplayName("post 정상적으로 추가할 수 있다.")
    void addPost() {
        // given
        PostDto.Add dto = PostDto.Add.builder()
            .categoryId(1L)
            .title("title")
            .content("content")
            .pictures(PictureFileUtil.createList("pic1", "pic2", "pic3"))
            .hashtags(Arrays.asList("hashtag1", "hashtag2"))
            .build();
        given(postRepository.save(any())).willReturn(
            Post.builder()
                .id(1L)
                .title("title")
                .template(1)
                .content("content")
                .build()
        );

        // when
        PostDto.ListGetElement addedPost = postService.addPost(dto);

        // then
        assertThat(addedPost.getPostId()).isEqualTo(1L);
        assertThat(addedPost.getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("post 추가하려 할 때 category 없으면 CATEGORY_NOT_FOUND_ERROR 발생")
    void addPost_x() {
        // given
        PostDto.Add dto = PostDto.Add.builder()
            .categoryId(1L)
            .title("title")
            .content("content")
            .build();
        given(postRepository.save(any())).willThrow(DataIntegrityViolationException.class);

        // when
        // then
        BusinessException e = assertThrows(BusinessException.class, () -> postService.addPost(dto));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND_ERROR);
    }

    @Test
    @DisplayName("post 수정을 할 수 있다.")
    void updatePost()
    {
        // given
        Post post = Post.builder()
            .id(1L)
            .title("title")
            .template(1)
            .content("content")
            .pictures(
                Arrays.asList(Picture.builder().url("pic1").build()
                , Picture.builder().url("pic2").build())
            )
            .postHashtags(Arrays.asList(
                PostHashtag.builder()
                    .id(1L)
                    .hashtag(new Hashtag("hashtag1"))
                    .build()
                , PostHashtag.builder()
                    .id(2L)
                    .hashtag(new Hashtag("hashtag2"))
                    .build()
                )
            )
            .build();

        PostDto.Update dto = PostDto.Update.builder()
            .title("newTitle")
            .content("newTitle")
            .template(1)
            .hashtags(Collections.singletonList("hashtag2"))
            .addPics(PictureFileUtil.createList("pic3", "pic4"))
            .delPics(Collections.singletonList("pic1"))
            .build();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(hashtagRepository.save(any())).willReturn(new Hashtag("hashtag2"));
        given(googleDriveUtil.filesToURLs(any(), any())).willReturn(
            Arrays.asList("pic3", "pic4")
        );

        // when
        PostDto.GetOne result = postService.updatePost(1L, dto);

        // then
        assertThat(result.getTitle()).isEqualTo(dto.getTitle());
        assertThat(result.getContent()).isEqualTo(dto.getContent());
        assertThat(result.getPictures()).hasSize(3);
    }

    @Test
    @DisplayName("정상적으로 게시글을 삭제할 수 있고, 연관된 picture, comment, hashtag 모두 삭제 처리된다.")
    void deletePost() {
        // given
        Post post = Post.builder()
            .id(1L)
            .title("title")
            .template(1)
            .content("content")
            .pictures(
                Arrays.asList(Picture.builder().url("pic1").build()
                    , Picture.builder().url("pic2").build())
            )
            .comments(
                Arrays.asList(
                    Comment.builder().content("comment1").build()
                    , Comment.builder().content("comment2").build()
                )
            )
            .postHashtags(Arrays.asList(
                    PostHashtag.builder()
                        .id(1L)
                        .hashtag(new Hashtag("hashtag1"))
                        .build()
                    , PostHashtag.builder()
                        .id(2L)
                        .hashtag(new Hashtag("hashtag2"))
                        .build()
                )
            )
            .build();
        given(postRepository.findJoining(anyLong())).willReturn(Optional.of(post));

        // when
        postService.deletePost(1L);

        // then
        assertThat(post.getDeleteFlag().isDeleted()).isTrue();
        post.getComments().forEach(
            comment -> assertThat(comment.getDeleteFlag().isDeleted()).isTrue()
        );
        post.getPostHashtags().forEach(
            postHashtag -> assertThat(postHashtag.getDeleteFlag().isDeleted()).isTrue()
        );
        post.getPictures().forEach(
            picture -> assertThat(picture.getDeleteFlag().isDeleted()).isTrue()
        );
    }
}