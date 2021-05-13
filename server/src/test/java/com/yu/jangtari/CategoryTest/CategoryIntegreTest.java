package com.yu.jangtari.CategoryTest;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.repository.category.CategoryRepository;
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
    private CategoryRepository categoryRepository;

    @Nested
    @DisplayName("성공 테스트")
    class SuccessTest {
        @Test
        @DisplayName("category 추가 성공(picture X)")
        void post_O1() throws Exception {
            CategoryDTO.Add categoryDTO = makeCategoryDTOwithoutPicture();
            mockMvc.perform(multipart("/admin/category").param("name", categoryDTO.getName()))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("category"))
                    .andExpect(jsonPath("$.picture").doesNotExist()) // null 검사
                    .andDo(print());
        }
        @Test
        @DisplayName("category 수정 성공(picture X)")
        void put_O1() throws Exception {
            // given
            CategoryDTO.Add addDTO = makeCategoryDTOwithPicture();
            Category category = categoryRepository.save(addDTO.toEntity("url"));

            CategoryDTO.Update categoryDTO = makeUpdateCategoryDTOwithoutPicture();
            mockMvc.perform(multipart("/admin/category/1").param("name", categoryDTO.getName()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("updated"))
                    .andExpect(jsonPath("$.picture").value("url"))
                    .andDo(print());
        }
    }
    @Nested
    @DisplayName("실패 테스트")
    class FailureTest {
        @Test
        @DisplayName("category 추가 실패 - name X")
        void post_X1() throws Exception {
            mockMvc.perform(multipart("/admin/category").param("name", ""))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
        @Test
        @DisplayName("category 수정 실패 - name X")
        void put_X1() throws Exception {
            mockMvc.perform(multipart("/admin/category/1").param("name", ""))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
        @Test
        @DisplayName("category 수정 실패 - NoSuchCategoryException")
        void put_X2() throws Exception {
            mockMvc.perform(multipart("/admin/category/1").param("name", "aa"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("No Such Category"))
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.errors").doesNotExist())
                    .andExpect(jsonPath("$.code").value("yu007"))
                    .andDo(print());
        }
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
    private CategoryDTO.Update makeUpdateCategoryDTOwithoutPicture() {
        return CategoryDTO.Update.builder()
                .name("updated")
                .build();
    }
}
