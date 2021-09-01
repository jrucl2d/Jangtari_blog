package com.yu.jangtari.api.category.controller;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.category.repository.CategoryRepository;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.security.jwt.JwtInfo;
import com.yu.jangtari.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest extends IntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private static String accessToken;

    @BeforeEach
    void setUp() {
        accessToken = JwtUtil.createAccessToken(JwtInfo.builder()
            .username("username")
            .nickName("nick")
            .roleType(RoleType.ADMIN)
            .build());
    }

    @Test
    @DisplayName("category 정상적으로 추가할 수 있다.")
    void addCategory() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "category1");

        mockMvc.perform(post("/admin/category")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(params))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("category1"))
            .andDo(print());
    }

    @Test
    @DisplayName("category 정상적으로 수정할 수 있다.")
    void updateCategory() throws Exception {
        Category category = categoryRepository.save(Category
            .builder()
            .name("category")
            .build());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("categoryId", String.valueOf(category.getId()));
        params.add("name", "newCategory");

        mockMvc.perform(put("/admin/category")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(params))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.categoryId").value(category.getId()))
            .andExpect(jsonPath("$.name").value("newCategory"))
            .andDo(print());
    }

    @Test
    @DisplayName("없는 category 수정할 수 없다.")
    void updateCategory_x() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("categoryId", "1000000");
        params.add("name", "newCategory");

        mockMvc.perform(put("/admin/category")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(params))
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.code").value(ErrorCode.CATEGORY_NOT_FOUND_ERROR.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.CATEGORY_NOT_FOUND_ERROR.getMessage()))
            .andDo(print());
    }

    // TODO : post 넣는 service 테스트 후 진행
    @Test
    @DisplayName("category 삭제하면 연관된 post 등 전부 삭제 처리")
    void deleteCategory()
    {
        // given

        // when

        // then
    }
}