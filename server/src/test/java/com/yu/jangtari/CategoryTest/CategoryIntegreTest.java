package com.yu.jangtari.CategoryTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryIntegreTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("성공 테스트")
    class SuccessTest {
        @Test
        @DisplayName("POST /admin/category - picture X 성공")
        void post_O1() throws Exception {
            CategoryDTO.Add categoryDTO = makeCategoryDTOwithoutPicture();
            mockMvc.perform(multipart("/admin/category").param("name", categoryDTO.getName()))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("category"))
                    .andExpect(jsonPath("$.picture").doesNotExist()) // null 검사
                    .andDo(print());
        }
    }
    @Nested
    @DisplayName("실패 테스트")
    class FailureTest {

    }

    private CategoryDTO.Add makeCategoryDTOwithPicture() {
        return CategoryDTO.Add.builder()
                .name("category")
                .picture(new MockMultipartFile("pic1", new byte[]{0}))
                .build();
    }
    private CategoryDTO.Add makeCategoryDTOwithoutPicture() {
        return CategoryDTO.Add.builder()
                .name("category")
                .build();
    }
}
