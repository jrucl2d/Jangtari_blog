package com.yu.jangtari.PostTest;

import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.domain.Hashtag;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.domain.PostHashtag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostDomainTest {
    @Nested
    @DisplayName("성공 테스트")
    class SuccessTest {
        @Test
        @DisplayName("PostDTO.Add toEntity() 성공")
        void toEntity_O() {
            Post post = makePost();
            assertThat(post.getTitle()).isEqualTo("post title");
            assertThat(post.getContent()).isEqualTo("post content");
            assertThat(post.getCategory().getName()).isEqualTo("category");
            assertThat(post.getTemplate()).isEqualTo(1);
        }
        @Test
        @DisplayName("PostDTO.Add getHashtags() 성공")
        void getHashtags_O() {
            PostDTO.Add postDTO = makePostDTO();
            assertThat(postDTO.getHashtags().size()).isEqualTo(2);
        }
        @Test
        @DisplayName("Post initPictures() 성공")
        void initPictures_O() {
            // given
            Post post = makePost();

            // when
            assertThat(post.getPictures().size()).isEqualTo(0);
            post.initPictures(Arrays.asList("haha", "haha2"));
            // then
            assertThat(post.getPictures().size()).isEqualTo(2);
        }
        @Test
        @DisplayName("Post initPostHashtags() 성공")
        void initPostHashtags_O() {
            // given
            Post post = makePost();
            List<Hashtag> hashtags = makeHashtags();
            assertThat(post.getPostHashtags().size()).isEqualTo(0);
            // when
            post.initPostHashtags(hashtags);
            // then
            assertThat(post.getPostHashtags().size()).isEqualTo(2);
            for (PostHashtag postHashtag : post.getPostHashtags()) {
                assertThat(postHashtag.getPost()).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("실패 테스트")
    class FailureTest {

    }

    private List<Hashtag> makeHashtags() {
        return Arrays.asList(new Hashtag("aa"), new Hashtag("bb"));
    }
    private Post makePost() {
        PostDTO.Add postDTO = makePostDTO();
        Category category = makeCategory();
        return postDTO.toEntity(category);
    }
    private PostDTO.Add makePostDTO() {
        return PostDTO.Add.builder()
                .title("post title")
                .content("post content")
                .categoryId(1L)
                .template(1)
                .hashtags(Arrays.asList(new String[] {"aaa", "bbb"}))
                .pictures(Arrays.asList(new MockMultipartFile("mock", new byte[]{0}),
                        new MockMultipartFile("mock2", new byte[]{0}))).build();
    }
    private Category makeCategory() {
        return Category.builder().name("category").picture("catpic").build();
    }
}
