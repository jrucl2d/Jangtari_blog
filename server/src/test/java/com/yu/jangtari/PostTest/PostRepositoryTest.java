package com.yu.jangtari.PostTest;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.domain.*;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.repository.CommentRepository;
import com.yu.jangtari.repository.HashtagRepository;
import com.yu.jangtari.repository.PictureRepository;
import com.yu.jangtari.repository.PostHashtagRepository;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.member.MemberRepository;
import com.yu.jangtari.repository.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PostRepositoryTest extends IntegrationTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostHashtagRepository postHashtagRepository;
    @Autowired
    private PictureRepository pictureRepository;

    @Test
    @DisplayName("getOne 테스트 성공")
    void getOne_O() {
        // given
        Category category = makeCategory();
        Post post = makePost(category, null, null, 1);
        // when
        Post findPost = postRepository.getOne(1L).get();
        // then
        assertThat(post).isEqualTo(findPost);
    }
    @Test
    @DisplayName("getOne hashtag, picture 같이 가져오기 성공")
    void getOne1_O() {
        // given
        Category category = makeCategory();
        List<Hashtag> hashtags = Arrays.asList(new Hashtag("aaa"), new Hashtag("bbb"));
        List<String> pictures = Arrays.asList("picture1", "picture2");
        Post post = makePost(category, hashtags, pictures, 1);
        postRepository.save(post);
        // when
        Post findPost = postRepository.getOne(1L).get();
        // then
        assertThat(post).isEqualTo(findPost);
    }
    @Test
    @DisplayName("getOne 삭제된 comment 안 불러오기")
    void getOne2_O() {
        // given
        Category category = makeCategory();
        List<Hashtag> hashtags = Arrays.asList(new Hashtag("aaa"), new Hashtag("bbb"));
        List<String> pictures = Arrays.asList("picture1", "picture2");
        Post post = makePost(category, hashtags, pictures, 1);
        post.addComment(Comment.builder().content("c1").build());
        post.addComment(Comment.builder().content("c2").build());
        post.getComments().get(1).getDeleteFlag().softDelete();
        postRepository.save(post);
        // when
        Post findPost = postRepository.getOne(1L).get();
        // then
        assertThat(post).isEqualTo(findPost);
        System.out.println("호호");
        System.out.println(findPost);
    }
    @Test
    @DisplayName("getOne 테스트 실패 - 존재하지 않는 post를 찾을 시 NoSuchElementException")
    void getOne_X() {
        // given, when, then
        assertThrows(NoSuchElementException.class, () -> postRepository.getOne(1L).get());
    }
    @Test
    @DisplayName("getOne 테스트 실패 - 삭제된 post를 찾을 시 NoSuchElementException")
    void getOne1_X() {
        // given
        Category category = makeCategory();
        List<Hashtag> hashtags = Arrays.asList(new Hashtag("aaa"), new Hashtag("bbb"));
        List<String> pictures = Arrays.asList("picture1", "picture2");
        Post post = makePost(category, hashtags, pictures, 1);
        // 삭제 처리
        post.getDeleteFlag().softDelete();
        postRepository.save(post);
        // when, then
        assertThrows(NoSuchElementException.class, () -> postRepository.getOne(1L).get());
    }
    @Test
    @DisplayName("getPostListForDelete 테스트 성공")
    void getPostListForDelete_O() {
        // given
        Category category = makeCategory();
        Post post1 = makePost(category, null, null, 1);
        Post post2 = makePost(category, null, null, 2);
        // when
        List<Post> posts = postRepository.getPostListForDelete(category.getId());
        // then
        assertThat(post1).isEqualTo(posts.get(0));
        assertThat(post2).isEqualTo(posts.get(1));
    }
    @Test
    @DisplayName("post add Test 성공 -> Integration test로 옮기는게 좋을듯")
    void addPost_O() {
        // given
        Category category = makeCategory();
        Member member = Member.builder().username("username").nickname("nickname").password("password").build();
        member = memberRepository.save(member);
        List<String> pictures = Arrays.asList("picture1", "picture2");
        List<Hashtag> hashtags = Arrays.asList(new Hashtag("h1"), new Hashtag("h2"));
        Post post = makePost(category, hashtags, pictures, 1);
        makeComment(post, member);
        // when
        Post findPost = postRepository.findById(1L).get();
//        // then
        assertThat(findPost.getPostHashtags().size()).isEqualTo(2);
        assertThat(findPost.getComments().size()).isEqualTo(1);
        assertThat(findPost.getPictures().size()).isEqualTo(2);
    }
    @Test
    @DisplayName("getPostList() title로 찾기")
    void getPostList1_O() {
        // given
        Category category = makeCategory();
        List<Hashtag> hashtags = Arrays.asList(new Hashtag("aaa"), new Hashtag("bbb"));
        makePost(category, hashtags, null, 1);
        Post post2 = makePost(category, hashtags, null, 2);
        Post post3 = makePost(category, hashtags, null, 2);
        PageRequest pageRequest = new PageRequest(1, "t", "title2");
        // when
        Page<PostDTO.Get> posts = postRepository.getPostList(1L, pageRequest);
        List<PostDTO.Get> postList = posts.toList();
        // then
        assertThat(posts.getTotalElements()).isEqualTo(2);
        assertThat(postList.get(0).getPostId()).isEqualTo(post2.getId());
        assertThat(postList.get(0).getTitle()).isEqualTo(post2.getTitle());
        assertThat(postList.get(1).getPostId()).isEqualTo(post3.getId());
        assertThat(postList.get(1).getTitle()).isEqualTo(post3.getTitle());    }
    @Test
    @DisplayName("getPostList() content로 찾기")
    void getPostList2_O() {
        // given
        Category category = makeCategory();
        List<Hashtag> hashtags = Arrays.asList(new Hashtag("aaa"), new Hashtag("bbb"));
        makePost(category, hashtags, null, 1);
        makePost(category, hashtags, null, 2);
        makePost(category, hashtags, null, 2);
        PageRequest pageRequest = new PageRequest(1, "c", "tent1");
        // when
        Page<PostDTO.Get> posts = postRepository.getPostList(1L, pageRequest);
        // then
        assertThat(posts.getTotalElements()).isEqualTo(1);
    }
    @Test
    @DisplayName("getPostList() hashtag로 찾기")
    void getPostList3_O() {
        // given
        Category category = makeCategory();
        List<Hashtag> hashtags = Arrays.asList(new Hashtag("aaa"), new Hashtag("bbb"));
        makePost(category, hashtags, null, 1);
        makePost(category, hashtags, null, 2);
        makePost(category, hashtags, null, 2);
        PageRequest pageRequest = new PageRequest(1, "h", "aaa");
        // when
        Page<PostDTO.Get> posts = postRepository.getPostList(1L, pageRequest);
        // then
        assertThat(posts.getTotalElements()).isEqualTo(3);
    }
    @Test
    @DisplayName("getPostList() 없는 hashtag로 찾기")
    void getPostList4_O() {
        // given
        Category category = makeCategory();
        List<Hashtag> hashtags = Arrays.asList(new Hashtag("aaa"), new Hashtag("bbb"));
        makePost(category, hashtags, null, 1);
        makePost(category, hashtags, null, 2);
        makePost(category, hashtags, null, 2);
        PageRequest pageRequest = new PageRequest(1, "h", "cc");
        // when
        Page<PostDTO.Get> posts = postRepository.getPostList(1L, pageRequest);
        // then
        assertThat(posts.getTotalElements()).isEqualTo(0);
    }
    @Test
    @DisplayName("getPostList() 조건 없이 찾기")
    void getPostList5_O() {
        // given
        Category category = makeCategory();
        List<Hashtag> hashtags = Arrays.asList(new Hashtag("aaa"), new Hashtag("bbb"));
        makePost(category, hashtags, null, 1);
        makePost(category, hashtags, null, 2);
        makePost(category, hashtags, null, 2);
        PageRequest pageRequest = new PageRequest(1, null, null);
        // when
        Page<PostDTO.Get> posts = postRepository.getPostList(1L, pageRequest);
        // then
        assertThat(posts.getTotalElements()).isEqualTo(3);
    }
    @Test
    @DisplayName("getPostList() delete된 post는 안 찾기")
    void getPostList6_O() {
        // given
        Category category = makeCategory();
        List<Hashtag> hashtags = Arrays.asList(new Hashtag("aaa"), new Hashtag("bbb"));
        Post post1 = makePost(category, hashtags, null, 1);
        makePost(category, hashtags, null, 1);
        makePost(category, hashtags, null, 1);

        post1.getDeleteFlag().softDelete();
        postRepository.save(post1);

        PageRequest pageRequest = new PageRequest(1, "t", "title1");
        // when
        Page<PostDTO.Get> posts = postRepository.getPostList(1L, pageRequest);
        // then
        assertThat(posts.getTotalElements()).isEqualTo(2);
    }
    private Comment makeComment(Post post, Member member) {
        Comment comment = Comment.builder().content("content").build();
        comment.initPostAndMember(post, member);
        commentRepository.save(comment);
        return comment;
    }
    private Post makePost(Category category, List<Hashtag> hashtags, List<String> pictures, int index) {
        final Post post = Post.builder().category(category).content("content"+index).template(1).title("title"+index).build();
        postRepository.save(post);
        if (hashtags == null && pictures == null) return post;
        if (pictures != null) {
            List<Picture> pictureList = Picture.stringsToPictures(pictures, post);
            pictureRepository.saveAll(pictureList);
            post.addPictures(pictureList);
        }
        hashtagRepository.saveAll(hashtags);
        List<PostHashtag> postHashtags = PostHashtag.hashtagsToPostHashtags(hashtags, post);
        postHashtagRepository.saveAll(postHashtags);
        post.addPostHashtags(postHashtags);
        return post;
    }
    private Category makeCategory() {
        Category category = Category.builder().name("category").picture("picture").build();
        return categoryRepository.save(category);
    }
}