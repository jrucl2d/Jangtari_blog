package com.yu.jangtari.PostTest;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.domain.Picture;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.post.PostRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        preTask_Post_Add();

        // Post Update 과정
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", "mtitle");
        map.add("content", "mcontent");
        map.add("template", "0");
        map.add("hashtags", "hashtag2");
        map.add("hashtags", "hashtag3");
        map.add("delPics", "pic1");
        mockMvc.perform(multipart("/admin/post/1")
                .params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("mtitle"))
                .andExpect(jsonPath("$.content").value("mcontent"))
                .andExpect(jsonPath("$.pictures.[0].picture").value("pic2"))
                .andExpect(jsonPath("$.hashtags.[0]").value("hashtag2"))
                .andExpect(jsonPath("$.hashtags.[1]").value("hashtag3"))
                .andDo(print());
    }
    @Test
    @DisplayName("deletePost O, 포스트 삭제")
    void deletePost_O() throws Exception {
        // Post Add 과정
        preTask_Post_Add();

        // Post Delete 과정
        mockMvc.perform(delete("/admin/post/1"))
                .andExpect(status().isOk())
                .andDo(print());

        Post deletedPost = postRepository.findById(1L).get();
        assertThat(deletedPost.getDeleteFlag().isDeleteFlag()).isTrue();
        deletedPost.getComments().forEach(c -> assertThat(c.getDeleteFlag().isDeleteFlag()).isTrue());
        deletedPost.getPictures().forEach(p -> assertThat(p.getDeleteFlag().isDeleteFlag()).isTrue());
        deletedPost.getPostHashtags().forEach(p -> assertThat(p.getDeleteFlag().isDeleteFlag()).isTrue());
    }
    @Test
    @DisplayName("deletePost X, 없는 포스트 삭제하려고 할 때 실")
    void deletePost_X() throws Exception {
        // Post Delete 과정
        mockMvc.perform(delete("/admin/post/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No Such Post"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.code").value("yu010"))
                .andDo(print());
    }
    @Test
    @DisplayName("getPost O, 포스트 가져오기 성공")
    void getPost_O() throws Exception {
        preTask_Post_Add();

        mockMvc.perform(get("/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.pictures").value(Matchers.hasSize(2)))
                .andExpect(jsonPath("$.hashtags").value(Matchers.hasSize(2)))
                .andDo(print());
    }
    @Test
    @DisplayName("getPost X, 없는 포스트 가져오기 실패")
    void getPost_X() throws Exception {
        mockMvc.perform(get("/post/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No Such Post"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.code").value("yu010"))
                .andDo(print());
    }
    @Nested
    @DisplayName("getPostList 테스트")
    class GetPostList {
        @Test
        @DisplayName("getPostList O, 내용으로 포스트 목록 가져오기 성공")
        void getPostList_O() throws Exception {
            preTask_Post_Add();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("title", "title2");
            map.add("content", "content2");
            map.add("template", "0");
            map.add("categoryId", "1");
            mockMvc.perform(multipart("/admin/post")
                    .params(map))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/category/1/posts?page=1&type=c&keyword=con"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value(Matchers.hasSize(2)))
                    .andDo(print());
            mockMvc.perform(get("/category/1/posts?page=1&type=c&keyword=content2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value(Matchers.hasSize(1)))
                    .andDo(print());
        }
        @Test
        @DisplayName("getPostList O, 제목으로 포스트 목록 가져오기 성공")
        void getPostList_O1() throws Exception {
            preTask_Post_Add();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("title", "title2");
            map.add("content", "content2");
            map.add("template", "0");
            map.add("categoryId", "1");
            mockMvc.perform(multipart("/admin/post")
                    .params(map))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/category/1/posts?page=1&type=t&keyword=tit"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value(Matchers.hasSize(2)))
                    .andDo(print());
            mockMvc.perform(get("/category/1/posts?page=1&type=t&keyword=title2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value(Matchers.hasSize(1)))
                    .andDo(print());
        }
        @Test
        @DisplayName("getPostList O, 해시태그로 포스트 목록 가져오기 성공")
        void getPostList_O2() throws Exception {
            preTask_Post_Add();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("title", "title2");
            map.add("content", "content2");
            map.add("template", "0");
            map.add("categoryId", "1");
            map.add("hashtags", "hashtag2"); // 공통된 해시태그
            map.add("hashtags", "hashtag4");
            mockMvc.perform(multipart("/admin/post")
                    .params(map))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/category/1/posts?page=1&type=h&keyword=hashtag2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value(Matchers.hasSize(2)))
                    .andDo(print());
            mockMvc.perform(get("/category/1/posts?page=1&type=h&keyword=hashtag4"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value(Matchers.hasSize(1)))
                    .andDo(print());
        }
        @Test
        @DisplayName("getPostList O, 다음 페이지 가져오면 빈 목록")
        void getPostList_O3() throws Exception {
            preTask_Post_Add();
            mockMvc.perform(get("/category/1/posts?page=2&type=c&keyword=content"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value(Matchers.hasSize(0)))
                    .andDo(print());
        }
        @Test
        @DisplayName("getPostList O, 0 이하의 페이지 요구하면 첫 번째 페이지")
        void getPostList_O4() throws Exception {
            preTask_Post_Add();
            mockMvc.perform(get("/category/1/posts?page=0&type=c&keyword=content"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value(Matchers.hasSize(1)))
                    .andDo(print());
        }
        @Test
        @DisplayName("getPostList X, 없는 타입으로 검색하면 Search type error 발생")
        void getPostList_X() throws Exception {
            preTask_Post_Add();
            mockMvc.perform(get("/category/1/posts?page=0&type=k&keyword=content"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("No Such SearchType"))
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.errors").doesNotExist())
                    .andExpect(jsonPath("$.code").value("yu013"))
                    .andDo(print());
        }
    }
    private PostDTO.Add getPostDTO() {
        return PostDTO.Add.builder()
                .title("title")
                .content("content")
                .template(1)
                .categoryId(1L)
                .build();
    }
    private void preTask_Post_Add() throws Exception {
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
                .andExpect(status().isCreated());
        Post savedPost = postRepository.findById(1L).get();
        savedPost.addPictures(Arrays.asList(Picture.builder().post(savedPost).url("pic1").build(),
                Picture.builder().post(savedPost).url("pic2").build()));
        postRepository.save(savedPost);
    }
}
