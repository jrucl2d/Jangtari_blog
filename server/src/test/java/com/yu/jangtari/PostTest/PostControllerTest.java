package com.yu.jangtari.PostTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.domain.Picture;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("addPost O, 포스트 추가 성공")
    void addPost_O() throws Exception {
        Category category = Category.builder().name("category").build();
        categoryRepository.save(category);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", "title");
        map.add("content", "content");
        map.add("template", "1");
        map.add("categoryId", "1");
        map.add("hashtags", "hashtag1");
        map.add("hashtags", "hashtag2");
        mockMvc.perform(multipart("/admin/post")
                .params(map))
                .andExpect(status().isCreated())
                .andDo(print());
    }
    @Test
    @DisplayName("addPost X, 없는 카테고리에 포스트 추가하려고 할 때 실패")
    void addPost_X() throws Exception {
        PostDTO.Add postDTO = getPostDTO();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", postDTO.getTitle());
        map.add("content", postDTO.getContent());
        map.add("template", String.valueOf(postDTO.getTemplate()));
        map.add("categoryId", String.valueOf(postDTO.getCategoryId()));
        mockMvc.perform(multipart("/admin/post")
                .params(map))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No Such Category"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.code").value("yu007"))
                .andDo(print());
    }
    @Test
    @DisplayName("addPost X, 필수로 넣어야 하는 categoryId, title이 없을 때 실패")
    void addPost_X2() throws Exception {
        Category category = Category.builder().name("category").build();
        categoryRepository.save(category);
        PostDTO.Add postDTO = getPostDTO();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", postDTO.getTitle());
        map.add("content", postDTO.getContent());
        map.add("template", String.valueOf(postDTO.getTemplate()));
        // categoryId가 없을 때
        mockMvc.perform(multipart("/admin/post")
                .params(map))
                .andExpect(status().isBadRequest())
                .andDo(print());
        // title 없을 때
        map.remove("title");
        map.add("categoryId", String.valueOf(postDTO.getCategoryId()));
        mockMvc.perform(multipart("/admin/post")
                .params(map))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @DisplayName("updatePost O, 포스트 수정 성공")
    void updatePost_O() throws Exception {
        // Post Add 과정
        Category category = Category.builder().name("category").build();
        categoryRepository.save(category);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", "title");
        map.add("content", "content");
        map.add("template", "1");
        map.add("categoryId", "1");
        map.add("hashtags", "hashtag1");
        map.add("hashtags", "hashtag2");

        mockMvc.perform(multipart("/admin/post")
                .params(map))
                .andExpect(status().isCreated())
                .andDo(print());

        Post savedPost = postRepository.findById(1L).get();
        savedPost.addPictures(Arrays.asList(Picture.builder().post(savedPost).url("pic1").build(),
                Picture.builder().post(savedPost).url("pic2").build()));
        postRepository.save(savedPost);
        // Post Update 과정
        map = new LinkedMultiValueMap<>();
        map.add("title", "mtitle");
        map.add("content", "mcontent");
        map.add("template", "0");
        map.add("hashtags", "hashtag2");
        map.add("hashtags", "hashtag3");
        map.add("delPics", "pic1");
        mockMvc.perform(multipart("/admin/post/1")
                .params(map))
                .andExpect(status().isOk())
                .andDo(print());
        Post modifiedPost = postRepository.findById(1L).get();
        assertThat(modifiedPost.getTemplate()).isEqualTo(0);
        assertThat(modifiedPost.getContent()).isEqualTo("mcontent");
        assertThat(modifiedPost.getPictures().size()).isEqualTo(1);
        assertThat(modifiedPost.getPictures().get(0).getUrl()).isEqualTo("pic2");
        assertThat(modifiedPost.getPostHashtags().get(0).getHashtag().getContent()).isEqualTo("hashtag2");
        assertThat(modifiedPost.getPostHashtags().get(1).getHashtag().getContent()).isEqualTo("hashtag3");
    }
    private PostDTO.Add getPostDTO() {
        return PostDTO.Add.builder()
                .title("title")
                .content("content")
                .template(1)
                .categoryId(1L)
                .build();
    }
}
