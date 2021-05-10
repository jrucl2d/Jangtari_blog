package com.yu.jangtari.PostTest;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.common.exception.FileTaskException;
import com.yu.jangtari.common.exception.GoogleDriveException;
import com.yu.jangtari.common.exception.NoSuchCategoryException;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.domain.Hashtag;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.repository.HashtagRepository;
import com.yu.jangtari.repository.post.PostRepository;
import com.yu.jangtari.service.CategoryService;
import com.yu.jangtari.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PostServiceTest extends ServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private CategoryService categoryService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private HashtagRepository hashtagRepository;
    @Mock
    private GoogleDriveUtil googleDriveUtil;

    @Nested
    @DisplayName("성공 테스트")
    class SuccessTest {
        @Test
        @DisplayName("addPost picture X, 성공")
        void addPost_without_picture_O() {
            // given
            PostDTO.Add postDTO = makePostDTOwithoutPicture();
            Category category = makeCategory();
            Post beforePost = makePost(false);
            List<Hashtag> hashtags = makeHashtags();
            given(categoryService.findOne(any())).willReturn(category);
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
        void addPost_with_picture_O() throws GeneralSecurityException, IOException {
            // given
            PostDTO.Add postDTO = makePostDTOwithPicture();
            Category category = makeCategory();
            Post beforePost = makePost(true);
            List<Hashtag> hashtags = makeHashtags();
            given(categoryService.findOne(any())).willReturn(category);
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
    }

    @Nested
    @DisplayName("실패 테스트")
    class FailureTest {
        @Test
        @DisplayName("addPost NoSuchCategoryException 발생")
        void addPost_No_Category_X() {
            // given
            PostDTO.Add postDTO = makePostDTOwithoutPicture();
            given(categoryService.findOne(any())).willThrow(NoSuchCategoryException.class);
            // when, then
            assertThrows(NoSuchCategoryException.class, () -> postService.addPost(postDTO));
        }
        @Test
        @DisplayName("addPost googleDriveUtil에서 GoogleDriveException 발생")
        void addPost_Google_Drive_Exception_X() {
            // given
            PostDTO.Add postDTO = makePostDTOwithPicture();
            Category category = makeCategory();
            given(categoryService.findOne(any())).willReturn(category);
            given(googleDriveUtil.filesToURLs(postDTO.getPictures(), GDFolder.POST)).willThrow(new GoogleDriveException());
            // when, then
            assertThrows(GoogleDriveException.class,() -> postService.addPost(postDTO));
        }
        @Test
        @DisplayName("addPost googleDriveUtil에서 FileTaskException 발생")
        void addPost_File_Task_Exception_X() {
            // given
            PostDTO.Add postDTO = makePostDTOwithPicture();
            Category category = makeCategory();
            given(categoryService.findOne(any())).willReturn(category);
            given(googleDriveUtil.filesToURLs(postDTO.getPictures(), GDFolder.POST)).willThrow(new FileTaskException());
            // when, then
            assertThrows(FileTaskException.class,() -> postService.addPost(postDTO));
        }
    }

    private PostDTO.Add makePostDTOwithoutPicture() {
        return PostDTO.Add.builder()
                .title("post title")
                .content("post content")
                .categoryId(1L)
                .template(1)
                .hashtags(Arrays.asList(new String[] {"aaa", "bbb"}))
                .pictures(Arrays.asList()).build();
    }
    private PostDTO.Add makePostDTOwithPicture() {
        return PostDTO.Add.builder()
                .title("post title")
                .content("post content")
                .categoryId(1L)
                .template(1)
                .hashtags(Arrays.asList(new String[] {"aaa", "bbb"}))
                .pictures(Arrays.asList(new MockMultipartFile("pic1", new byte[]{0}),
                        new MockMultipartFile("pic2", new byte[]{0}))).build();
    }
    private Post makePost(boolean withPicture) {
        PostDTO.Add postDTO = null;
        if (withPicture) {
            postDTO = makePostDTOwithPicture();
        } else {
            postDTO = makePostDTOwithoutPicture();
        }
        Category category = makeCategory();
        return postDTO.toEntity(category);
    }
    private List<Hashtag> makeHashtags() {
        return Arrays.asList(new Hashtag("aa"), new Hashtag("bb"));
    }
    private Category makeCategory() {
        return Category.builder().name("category").picture("catpic").build();
    }
}
