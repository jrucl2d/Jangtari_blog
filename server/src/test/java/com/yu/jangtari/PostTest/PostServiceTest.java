package com.yu.jangtari.PostTest;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.common.exception.FileTaskException;
import com.yu.jangtari.common.exception.GoogleDriveException;
import com.yu.jangtari.common.exception.NoSuchCategoryException;
import com.yu.jangtari.common.exception.NoSuchPostException;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.domain.*;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.repository.HashtagRepository;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.post.PostRepository;
import com.yu.jangtari.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class PostServiceTest extends ServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private HashtagRepository hashtagRepository;
    @Mock
    private GoogleDriveUtil googleDriveUtil;

    @Nested
    @DisplayName("addPost 테스트")
    class AddPostTest {
        @Test
        @DisplayName("addPost picture X, 성공")
        void addPost_without_picture_O() {
            // given
            PostDTO.Add postDTO = makeAddPostDTO();
            Category category = makeCategory();
            Post beforePost = makePost(false);
            List<Hashtag> hashtags = makeHashtags();
            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
            given(postRepository.save(any())).willReturn(beforePost);
            given(hashtagRepository.saveAll(any())).willReturn(hashtags);

            // when
            Post post = postService.addPost(postDTO);
            // then
            assertThat(post.getPostHashtags().size()).isEqualTo(2);
            for (int i = 0; i < 2; i++) {
                assertThat(post.getPostHashtags().get(i).getHashtag()).isEqualTo(hashtags.get(i));
                assertThat(post.getPostHashtags().get(i).getPost()).isEqualTo(post);
            }
            assertThat(post.getComments().size()).isEqualTo(0);
        }
        @Test
        @DisplayName("addPost picture O, 성공")
        void addPost_with_picture_O() {
            // given
            PostDTO.Add postDTO = makeAddPostDTO();
            Category category = makeCategory();
            Post beforePost = makePost(true);
            List<Hashtag> hashtags = makeHashtags();
            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
            given(postRepository.save(any())).willReturn(beforePost);
            given(hashtagRepository.saveAll(any())).willReturn(hashtags);
            given(googleDriveUtil.filesToURLs(postDTO.getPictures(), GDFolder.POST)).willReturn(Arrays.asList("pic1", "pic2"));
            // when
            Post post = postService.addPost(postDTO);
            // then
            assertThat(post.getPostHashtags().size()).isEqualTo(2);
            for (int i = 0; i < 2; i++) {
                assertThat(post.getPostHashtags().get(i).getHashtag()).isEqualTo(hashtags.get(i));
                assertThat(post.getPostHashtags().get(i).getPost()).isEqualTo(post);
            }
            assertThat(post.getComments().size()).isEqualTo(0);
            verify(googleDriveUtil, times(1)).filesToURLs(any(), any());
        }
        @Test
        @DisplayName("addPost NoSuchCategoryException 발생")
        void addPost_No_Category_X() {
            // given
            PostDTO.Add postDTO =  makeAddPostDTO();
            given(categoryRepository.findById(anyLong())).willThrow(NoSuchCategoryException.class);
            // when, then
            assertThrows(NoSuchCategoryException.class, () -> postService.addPost(postDTO));
        }
        @Test
        @DisplayName("addPost googleDriveUtil에서 GoogleDriveException 발생")
        void addPost_Google_Drive_Exception_X() {
            // given
            PostDTO.Add postDTO = makeAddPostDTO();
            Category category = makeCategory();
            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
            given(googleDriveUtil.filesToURLs(postDTO.getPictures(), GDFolder.POST)).willThrow(new GoogleDriveException());
            // when, then
            assertThrows(GoogleDriveException.class,() -> postService.addPost(postDTO));
        }
        @Test
        @DisplayName("addPost googleDriveUtil에서 FileTaskException 발생")
        void addPost_File_Task_Exception_X() {
            // given
            PostDTO.Add postDTO = makeAddPostDTO();
            Category category = makeCategory();
            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
            given(googleDriveUtil.filesToURLs(postDTO.getPictures(), GDFolder.POST)).willThrow(new FileTaskException());
            // when, then
            assertThrows(FileTaskException.class,() -> postService.addPost(postDTO));
        }
    }

    @Nested
    @DisplayName("getOne 테스트")
    class GetOneTest {
        @Test
        @DisplayName("postId로 getOne 성공")
        void getOne_O() {
            // given
            Post post = Post.builder().build();
            given(postRepository.getOne(anyLong())).willReturn(Optional.of(post));
            // when
            Post findPost = postService.getOne(1L);
            // then
            assertThat(post).isEqualTo(findPost);
        }
        @Test
        @DisplayName("없는 postId로 getOne시 NoSuchPostException발생")
        void getOne_X() {
            // given
            given(postRepository.getOne(anyLong())).willReturn(Optional.empty());
            // when
            // then
            assertThrows(NoSuchPostException.class, () -> postService.getOne(1L));
        }
    }
    @Nested
    @DisplayName("deletePost 테스트")
    class DeletePostTest {
        @Test
        @DisplayName("deletePost 수행시 연관된 comment, picture, postHastag 모두 delete 처리 성공")
        void deletePost1_O() {
            // given
            Post post = Post.builder().build();
            post.addPictures(Arrays.asList("picture1", "picture2"));
            post.addComment(Comment.builder().build());
            post.initPostHashtags(Arrays.asList(new Hashtag("h1"), new Hashtag("h2")));
            given(postRepository.getOne(anyLong())).willReturn(Optional.of(post));
            // when
            postService.deletePost(1L);
            // then
            assertThat(post.getDeleteFlag().isDeleteFlag()).isTrue();
            post.getPictures().forEach(picture -> assertThat(picture.getDeleteFlag().isDeleteFlag()).isTrue());
            post.getComments().forEach(comment -> assertThat(comment.getDeleteFlag().isDeleteFlag()).isTrue());
            post.getPostHashtags().forEach(postHashtag -> assertThat(postHashtag.getDeleteFlag().isDeleteFlag()).isTrue());
        }
        @Test
        @DisplayName("deletePost 수행시 연관된 comment, picture, postHastag 모두 delete 처리 성공")
        void deletePost2_O() {
            // given
            Post post = Post.builder().build();
            post.addPictures(Arrays.asList("picture1", "picture2"));
            post.addComment(Comment.builder().build());
            post.initPostHashtags(Arrays.asList(new Hashtag("h1"), new Hashtag("h2")));
            given(postRepository.getPostListForDelete(anyLong())).willReturn(Arrays.asList(post));
            // when
            postService.deletePostsOfCategory(1L);
            // then
            assertThat(post.getDeleteFlag().isDeleteFlag()).isTrue();
            post.getPictures().forEach(picture -> assertThat(picture.getDeleteFlag().isDeleteFlag()).isTrue());
            post.getComments().forEach(comment -> assertThat(comment.getDeleteFlag().isDeleteFlag()).isTrue());
            post.getPostHashtags().forEach(postHashtag -> assertThat(postHashtag.getDeleteFlag().isDeleteFlag()).isTrue());
        }
        @Test
        @DisplayName("deletePostsOfCategory 테스트 성공")
        void deletePostsOfCategory_O() {
            // given
            Post post1 = makePost(false);
            Post post2 = makePost(true);
            List<Hashtag> hashtags = makeHashtags();
            post2.initPostHashtags(hashtags);
            post2.addComment(Comment.builder().content("comment1").build());
            post2.addComment(Comment.builder().content("comment").build());
            given(postRepository.getPostListForDelete(anyLong())).willReturn(Arrays.asList(post1, post2));
            // when
            postService.deletePostsOfCategory(1L);
            assertThat(post1.getDeleteFlag().isDeleteFlag()).isTrue();
            assertThat(post2.getDeleteFlag().isDeleteFlag()).isTrue();
            post2.getPostHashtags().forEach(postHashtag -> assertThat(postHashtag.getDeleteFlag().isDeleteFlag()).isTrue());
            post2.getComments().forEach(comment -> assertThat(comment.getDeleteFlag().isDeleteFlag()).isTrue());
            post2.getPictures().forEach(picture -> assertThat(picture.getDeleteFlag().isDeleteFlag()).isTrue());
        }
    }
    @Nested
    @DisplayName("updatePost 테스트")
    class UpdatePostTest {
        @Test
        @DisplayName("updatePost에서 hashtag 개수 바뀜")
        void updatePost1_O() {
            // given
            PostDTO.Update postDTO = makeUpdatePostDTO(false);
            Post beforePost = makePost(true);
            given(postRepository.findById(any())).willReturn(Optional.of(beforePost));
            given(hashtagRepository.saveAll(any())).willReturn(postDTO.getHashtags());
            // when
            Post post = postService.updatePost(1L, postDTO);
            // then
            assertThat(post.getPostHashtags().size()).isEqualTo(3);
            assertThat(post.getPictures().size()).isEqualTo(2);
        }
        @Test
        @DisplayName("updatePost에서 hashtag 없애버림")
        void updatePost2_O() {
            // given
            PostDTO.Update postDTO = makeUpdatePostDTO(false);
            Post beforePost = makePost(false);
            given(postRepository.findById(any())).willReturn(Optional.of(beforePost));
            given(hashtagRepository.saveAll(any())).willReturn(Arrays.asList());
            // when
            Post post = postService.updatePost(1L, postDTO);
            // then
            assertThat(post.getPostHashtags().size()).isEqualTo(0);
            assertThat(post.getPictures().size()).isEqualTo(0);
        }
        @Test
        @DisplayName("updatePost에서 picture 바뀜")
        void updatePost3_O() {
            // given
            PostDTO.Update postDTO = makeUpdatePostDTO(true);
            Post beforePost = makePost(true);
            given(postRepository.findById(any())).willReturn(Optional.of(beforePost));
            given(hashtagRepository.saveAll(any())).willReturn(postDTO.getHashtags());
            given(googleDriveUtil.filesToURLs(postDTO.getAddPics(), GDFolder.POST)).willReturn(Collections.singletonList("pic3"));
            // when
            Post post = postService.updatePost(1L, postDTO);
            // then
            assertThat(post.getPictures().size()).isEqualTo(2);
            assertThat(post.getPictures().contains(Picture.builder().url("pic1").build())).isFalse();
            assertThat(post.getPictures().contains(Picture.builder().url("pic3").build())).isTrue();
        }
    }
    @Test
    void getPostList_O() {
        // given
        List<PostDTO.Get> posts = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> posts.add(PostDTO.Get.of(makePost(false))));
        Page<PostDTO.Get> postPage = new PageImpl<>(posts);
        given(postRepository.getPostList(anyLong(), any())).willReturn(postPage);
        // when
        Page<PostDTO.Get> resultPostPage = postService.getPostList(1L, new PageRequest(1, "h", "aaa"));
        // then
        assertThat(resultPostPage.getTotalElements()).isEqualTo(10);
    }

    private PostDTO.Update makeUpdatePostDTO(boolean withPicture) {
        if (withPicture) {
            return PostDTO.Update.builder()
                    .template(2)
                    .content("modified")
                    .title("modified title")
                    .delPics(Arrays.asList("pic1"))
                    .addPics(Arrays.asList(new MockMultipartFile("pic3", new byte[]{0})))
                    .hashtags(Arrays.asList("aaa", "bbb", "ccc"))
                    .build();
        }
        return PostDTO.Update.builder()
                .template(2)
                .content("modified")
                .title("modified title")
                .hashtags(Arrays.asList("aaa", "bbb", "ccc"))
                .build();
    }
    private PostDTO.Add makeAddPostDTO() {
        return PostDTO.Add.builder()
                .title("post title")
                .content("post content")
                .categoryId(1L)
                .template(1)
                .hashtags(Arrays.asList("aaa", "bbb", "ccc"))
                .build();
    }
    private Post makePost(boolean withPicture) {
        PostDTO.Add postDTO = makeAddPostDTO();
        Category category = makeCategory();
        if (withPicture) {
            final Post post = postDTO.toEntity(category);
            post.addPictures(Arrays.asList("pic1", "pic2"));
            return post;
        }
        return postDTO.toEntity(category);
    }
    private List<Hashtag> makeHashtags() {
        return Arrays.asList(new Hashtag("aa"), new Hashtag("bb"));
    }
    private Category makeCategory() {
        return Category.builder().name("category").picture("catpic").build();
    }
}
