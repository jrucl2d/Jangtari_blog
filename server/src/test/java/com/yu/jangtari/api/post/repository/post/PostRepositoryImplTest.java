package com.yu.jangtari.api.post.repository.post;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.category.repository.CategoryRepository;
import com.yu.jangtari.api.post.dto.PostDto;
import com.yu.jangtari.api.post.service.PostService;
import com.yu.jangtari.testHelper.PictureFileUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;

class PostRepositoryImplTest extends IntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryRepository categoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("findJoining 실행시 sql 잘 작동하는지 확인")
    void findJoining()
    {
        // given
        // when
        // then
        postRepository.findJoining(1L);
    }

    @Test
    void name()
    {
        // given
        categoryRepository.save(Category.builder().name("name").picture("picture").build());

        // when

        // then
        postService.addPost(PostDto.Add.builder().content("content").categoryId(1L)
            .pictures(PictureFileUtil.createList("pic1", "pic2"))
            .hashtags(Arrays.asList("hashtag1", "hashtag2"))
            .title("title").build());
        entityManager.flush();
    }
}