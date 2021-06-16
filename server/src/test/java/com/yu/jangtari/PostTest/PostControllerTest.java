package com.yu.jangtari.PostTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

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
    void addPost_O() throws Exception {
        Category category = Category.builder().name("category").build();
        categoryRepository.save(category);
        PostDTO.Add postDTO = PostDTO.Add.builder()
                .title("title")
                .content("content")
                .template(1)
                .categoryId(1L)
                .build();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", postDTO.getTitle());
        map.add("content", postDTO.getContent());
        map.add("template", String.valueOf(postDTO.getTemplate()));
        map.add("categoryId", String.valueOf(postDTO.getCategoryId()));
        mockMvc.perform(multipart("/admin/post")
                .params(map))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
