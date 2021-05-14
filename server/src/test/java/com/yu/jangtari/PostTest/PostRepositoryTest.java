package com.yu.jangtari.PostTest;

import com.yu.jangtari.RepositoryTest;
import com.yu.jangtari.domain.*;
import com.yu.jangtari.repository.CommentRepository;
import com.yu.jangtari.repository.HashtagRepository;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.member.MemberRepository;
import com.yu.jangtari.repository.post.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PostRepositoryTest extends RepositoryTest {
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

    @PersistenceContext
    EntityManager em;

    @AfterEach
    void afterEach() {
        em.createNativeQuery("alter table post alter column post_id restart with 1").executeUpdate();
    }

    @Test
    @DisplayName("getOne 테스트 성공")
    void getOne_O() {
        // given
        Category category = makeCategory();
        Post post = makePostInCategory(category);
        // when
        Post findPost = postRepository.getOne(1L).get();
        // then
        assertThat(post).isEqualTo(findPost);
    }
    @Test
    @DisplayName("getOne 테스트 실패 - 존재하지 않는 post를 찾을 시 NullPointerException")
    void getOne_X() {
        // given
        // when
        // then
        assertThrows(NullPointerException.class, () -> postRepository.getOne(1L).get());
    }
    @Test
    @DisplayName("post add Test 성공 -> Integration test로 옮기는게 좋을듯")
    void addPost_O() {
        // given
        Member member = Member.builder().username("username").nickname("nickname").password("password").build();
        member = memberRepository.save(member);
        Category category = makeCategory();
        Post post = makePostInCategoryWithPicturesAndHashtags(category);
        makeComment(post, member);
        // when
        Post findPost = postRepository.findById(1L).get();
        // then
        assertThat(findPost.getPostHashtags().size()).isEqualTo(2);
        assertThat(findPost.getComments().size()).isEqualTo(1);
        assertThat(findPost.getPictures().size()).isEqualTo(2);
    }
    private Comment makeComment(Post post, Member member) {
        Comment comment = Comment.builder().content("content").build();
        comment.initPostAndMember(post, member);
        commentRepository.save(comment);
        return comment;
    }
    private Post makePostInCategoryWithPicturesAndHashtags(Category category) {
        Post post = Post.builder().category(category).content("content").template(1).title("title").build();
        post.initPictures(Arrays.asList("picture1", "picture2"));
        post = postRepository.save(post);
        List<Hashtag> hashtags = hashtagRepository.saveAll(Arrays.asList(new Hashtag("h1"), new Hashtag("h2")));
        post.initPostHashtags(hashtags);
        return postRepository.save(post);
    }
    private Post makePostInCategory(Category category) {
        Post post = Post.builder().category(category).content("content").template(1).title("title").build();
        return postRepository.save(post);
    }
    private Category makeCategory() {
        Category category = Category.builder().name("category").picture("picture").build();
        return categoryRepository.save(category);
    }
}
