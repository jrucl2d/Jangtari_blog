package com.yu.jangtari.api.category.controller;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.security.jwt.JwtInfo;
import com.yu.jangtari.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest extends IntegrationTest {

    private static String accessToken;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil();
        accessToken = jwtUtil.createAccessToken(new JwtInfo("admin", RoleType.ADMIN));
    }

    @Test
    @DisplayName("category 정상적으로 추가할 수 있다.")
    void addCategory() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "category1");

        // when
        // then
        mockMvc.perform(multipart("/admin/category")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(params))
            .andExpect(status().isOk())
            .andDo(print());
    }
}