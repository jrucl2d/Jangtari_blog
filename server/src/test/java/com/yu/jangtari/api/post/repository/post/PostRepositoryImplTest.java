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
import java.util.Collections;

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
    @DisplayName("post add 하고 update 시 sql 잘 작동하는지 확인")
    void postSaveAndUpdate()
    {
        // addPost 확인
        categoryRepository.save(Category.builder().name("name").picture("picture").build());
        PostDto.ListGetElement savedPost = postService.addPost(PostDto.Add.builder()
            .title("title")
            .content("content")
            .template(0)
            .categoryId(1L)
            .pictures(PictureFileUtil.createList("pic1", "pic2"))
            .hashtags(Arrays.asList("hashtag1", "hashtag2"))
            .build());
        entityManager.flush();
        System.out.println(savedPost);

        PostDto.GetOne updatedPost = postService.updatePost(1L, PostDto.Update.builder()
            .title("new title")
            .content("new content")
            .template(1)
            .delPics(Collections.singletonList("pic1"))
            .addPics(PictureFileUtil.createList("pic3", "pic4", "pic5"))
            .hashtags(Arrays.asList("hashtag2", "hashtag3", "hashtag4"))
            .build());
        entityManager.flush();
        System.out.println(updatedPost);
    }
}