package com.yu.jangtari.CommentTest;

import com.yu.jangtari.RepositoryTest;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.Comment;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.repository.CommentRepository;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.member.MemberRepository;
import com.yu.jangtari.repository.post.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CommentRepositoryTest extends RepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CommentRepository commentRepository;

    @PersistenceContext
    EntityManager em;

    @AfterEach
    void afterEach() {
        em.createNativeQuery("alter table member alter column member_id restart with 1").executeUpdate();
        em.createNativeQuery("alter table category alter column category_id restart with 1").executeUpdate();
        em.createNativeQuery("alter table post alter column post_id restart with 1").executeUpdate();
        em.createNativeQuery("alter table comment alter column comment_id restart with 1").executeUpdate();
        em.createNativeQuery("alter table picture alter column picture_id restart with 1").executeUpdate();
        em.createNativeQuery("alter table post_hashtag alter column post_hashtag_id restart with 1").executeUpdate();
    }

    @Test
    @DisplayName("findCommentsOfPost 테스트 성공")
    void findCommentsOfPost_O() {
        // given
        Member member = memberRegister();
        Category category = makeCategory();
        Post post = makePostInCategory(category);
        Comment comment = makeCommentOfPost(member, post);
        // when
        List<Comment> comments = commentRepository.findCommentsOfPost(1L);
        // then
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0)).isEqualTo(comment);
    }
    @Test
    @DisplayName("findCommentsOfPost 테스트 성공 - deleted된 경우는 제외됨")
    void findCommentsOfPost_O2() {
        // given
        Member member = memberRegister();
        Category category = makeCategory();
        Post post = makePostInCategory(category);
        Comment comment = makeCommentOfPost(member, post);
        Comment comment2 = Comment.builder().content("content2").build();
        comment2.initPostAndMember(post, member);
        comment2.getDeleteFlag().softDelete();
        commentRepository.save(comment2);
        // when
        List<Comment> comments = commentRepository.findCommentsOfPost(1L);
        // then
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0)).isEqualTo(comment);
    }

    private Comment makeCommentOfPost(Member member, Post post) {
        Comment comment = Comment.builder().content("content").build();
        comment.initPostAndMember(post, member);
        return commentRepository.save(comment);
    }
    private Post makePostInCategory(Category category) {
        Post post = Post.builder().category(category).content("content").template(1).title("title").build();
        return postRepository.save(post);
    }
    private Category makeCategory() {
        Category category = Category.builder().name("category").picture("picture").build();
        return categoryRepository.save(category);
    }
    private Member memberRegister() {
        Member member = Member.builder().username("username").nickname("nickname").password("password").build();
        return memberRepository.save(member);
    }
}
