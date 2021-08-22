//package com.yu.jangtari.service;
//
//import com.yu.jangtari.ServiceTest;
//import com.yu.jangtari.common.exception.NoSuchPostException;
//import com.yu.jangtari.api.category.domain.Category;
//import com.yu.jangtari.api.post.dto.PostDto;
//import com.yu.jangtari.api.post.domain.Hashtag;
//import com.yu.jangtari.api.post.domain.Picture;
//import com.yu.jangtari.api.post.domain.Post;
//import com.yu.jangtari.api.post.domain.PostHashtag;
//import com.yu.jangtari.api.post.service.PostService;
//import com.yu.jangtari.api.post.repository.hashtag.HashtagRepository;
//import com.yu.jangtari.api.picture.repository.PictureRepository;
//import com.yu.jangtari.api.post.repository.hashtag.PostHashtagRepository;
//import com.yu.jangtari.api.category.repository.CategoryRepository;
//import com.yu.jangtari.api.post.repository.post.PostRepository;
//import com.yu.jangtari.util.GoogleDriveUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//class PostServiceTest extends ServiceTest {
//    @InjectMocks
//    private PostService postService;
//    @Mock
//    private CategoryRepository categoryRepository;
//    @Mock
//    private PostRepository postRepository;
//    @Mock
//    private HashtagRepository hashtagRepository;
//    @Mock
//    private PostHashtagRepository postHashtagRepository;
//    @Mock
//    private PictureRepository pictureRepository;
//    @Mock
//    private GoogleDriveUtil googleDriveUtil;
//
//    private Category category;
//    private Post originalPost;
//
//    @BeforeEach
//    void setUp() {
//        category = Category.builder().name("category").build();
//        originalPost = Post.builder().title("title").content("content").category(category).template(0).build();
//        originalPost.addPostHashtags(Arrays.asList(PostHashtag.builder()
//                .post(originalPost)
//                .hashtag(new Hashtag("hash1"))
//                .build(),
//            PostHashtag.builder()
//                .post(originalPost)
//                .hashtag(new Hashtag("hash2"))
//                .build()));
//        originalPost.addPictures(Arrays.asList(
//            Picture.builder().post(originalPost).url("pic1").build(),
//            Picture.builder().post(originalPost).url("pic2").build()
//        ));
//    }
//
//    // TODO : Repository 테스트 완료 후에 작성
//    @Test
//    void getOne() {
//        // given
//
//        // when
//
//        // then
//    }
//
//    // TODO : Repository 테스트 완료 후에 작성
//    @Test
//    void getPostList() {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    @DisplayName("picture과 hashtag도 포함하여 정상적으로 post 추가 성공")
//    void addPost() {
//        // given
//        PostDto.Add postDTO = PostDto.Add.builder().
//            title("title")
//            .content("content")
//            .categoryId(1L)
//            .hashtags(Arrays.asList("hash1", "hash2"))
//            .pictures(Arrays.asList(new MockMultipartFile("pic1", new byte[]{}),
//                new MockMultipartFile("pic2", new byte[]{})))
//            .template(0)
//            .build();
//
//        List<Hashtag> mockHashtags = new ArrayList<>();
//        List<PostHashtag> mockPostHashtags = new ArrayList<>();
//
//        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
//        given(postRepository.save(any())).willReturn(postDTO.toEntity(category));
//        given(hashtagRepository.saveAll(any())).willReturn(mockHashtags);
//        given(postHashtagRepository.saveAll(any())).willReturn(mockPostHashtags);
//        given(googleDriveUtil.filesToURLs(any(), any())).willReturn(Arrays.asList("pic1", "pic2"));
//        given(pictureRepository.saveAll(any())).willReturn(any());
//
//        // when
//        Post savedPost = postService.addPost(postDTO);
//
//        // then
//        verify(categoryRepository, times(1)).findById(1L);
//        verify(postRepository, times(1)).save(any());
//        verify(hashtagRepository, times(1)).saveAll(any());
//        verify(postHashtagRepository, times(1)).saveAll(any());
//        verify(googleDriveUtil, times(1)).filesToURLs(any(), any());
//        verify(pictureRepository, times(1)).saveAll(any());
//        assertEquals(2, savedPost.getPictures().size());
//        assertEquals(2, savedPost.getPostHashtags().size());
//    }
//
//    @Test
//    @DisplayName("hashtag와 picture를 포함해서 post 정보 모두 업데이트 성공")
//    void updatePost() {
//        // given
//        PostDto.Update postDTO = PostDto.Update
//            .builder()
//            .title("newTitle")
//            .content("newContent")
//            .template(1)
//            .addPics(Arrays.asList(new MockMultipartFile("pic3", new byte[]{}),
//                new MockMultipartFile("pic4", new byte[]{})))
//            .delPics(Arrays.asList("pic1", "pic2"))
//            .hashtags(Arrays.asList("hash2", "hash3"))
//            .build();
//
//        List<Picture> mockPictures = new ArrayList<>();
//        List<Hashtag> mockHashtags = new ArrayList<>();
//        List<PostHashtag> mockPostHashtags = new ArrayList<>();
//
//        given(postRepository.findById(anyLong())).willReturn(Optional.of(originalPost));
//        given(googleDriveUtil.filesToURLs(any(), any())).willReturn(Arrays.asList("pic3", "pic4"));
//        given(pictureRepository.saveAll(any())).willReturn(mockPictures);
//        given(hashtagRepository.saveAll(any())).willReturn(mockHashtags);
//        given(postHashtagRepository.saveAll(any())).willReturn(mockPostHashtags);
//
//        // when
//        Post updatedPost = postService.updatePost(1L, postDTO);
//
//        // then
//        verify(hashtagRepository, times(1)).saveAll(any());
//        verify(postHashtagRepository, times(1)).saveAll(any());
//        verify(googleDriveUtil, times(1)).filesToURLs(any(), any());
//        verify(pictureRepository, times(1)).saveAll(any());
//        assertEquals(2, updatedPost.getPictures().size());
//        assertEquals("pic3", updatedPost.getPictures().get(0).getUrl());
//        assertEquals("pic4", updatedPost.getPictures().get(1).getUrl());
//        assertEquals(2, updatedPost.getPostHashtags().size());
//        assertEquals("hash2", updatedPost.getPostHashtags().get(0).getHashtag().getContent());
//        assertEquals("hash3", updatedPost.getPostHashtags().get(1).getHashtag().getContent());
//    }
//
//    // TODO : Repository 테스트 완료 후에 작성
//    @Test
//    @DisplayName("없는 postId로 삭제하려고 하면 NoSuchPostException 발생")
//    void deletePost() {
//        // given
//        given(postRepository.findJoining(anyLong())).willReturn(Optional.empty());
//
//        // when
//        // then
//        assertThrows(NoSuchPostException.class, () -> postService.deletePost(1L));
//    }
//
//    // TODO : Repository 테스트 완료 후에 작성
//    @Test
//    void deletePostsOfCategory() {
//        // given
//
//        // when
//
//        // then
//    }
//}