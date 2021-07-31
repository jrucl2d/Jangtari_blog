//package com.yu.jangtari.CategoryTest;
//
//import com.yu.jangtari.IntegrationTest;
//import com.yu.jangtari.domain.RoleType;
//import com.yu.jangtari.util.CookieUtil;
//import com.yu.jangtari.util.JWTUtil;
//import com.yu.jangtari.domain.DTO.CategoryDTO;
//import com.yu.jangtari.repository.category.CategoryRepository;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//
//import javax.servlet.http.Cookie;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//public class CategoryControllerTest extends IntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Autowired
//    private CookieUtil cookieUtil;
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    @Nested
//    @DisplayName("성공 테스트")
//    class SuccessTest {
//        @Test
//        @DisplayName("category 추가 성공(picture X)")
//        void post_O1() throws Exception {
//            String accessToken = jwtUtil.createAccessToken("jangtari", RoleType.ADMIN);
//            Cookie cookie = cookieUtil.createAccessCookie(accessToken);
//
//            CategoryDTO.Add categoryDTO = makeCategoryDTOwithoutPicture();
//            mockMvc.perform(multipart("/admin/category").param("name", categoryDTO.getName()).cookie(cookie))
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.name").value("category"))
//                    .andExpect(jsonPath("$.picture").doesNotExist()) // null 검사
//                    .andDo(print());
//        }
//        @Test
//        @DisplayName("category 수정 성공 사진은 수정하지 않음")
//        void put_O1() throws Exception {
//            // given
//            String accessToken = jwtUtil.createAccessToken("jangtari", RoleType.ADMIN);
//            Cookie cookie = cookieUtil.createAccessCookie(accessToken);
//            CategoryDTO.Add addDTO = makeCategoryDTOwithPicture();
//            categoryRepository.save(addDTO.toEntity());
//
//            CategoryDTO.Update categoryDTO = makeUpdateCategoryDTOwithoutPicture();
//            mockMvc.perform(multipart("/admin/category/1").param("name", categoryDTO.getName()).cookie(cookie))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.name").value("updated"))
//                    .andExpect(jsonPath("$.picture").value("url"))
//                    .andDo(print());
//        }
//        @Test
//        @DisplayName("전체 category 가져오기")
//        void getAllCategories() throws Exception {
//            CategoryDTO.Add addDTO = makeCategoryDTOwithoutPicture();
//            categoryRepository.save(addDTO.toEntity());
//            categoryRepository.save(addDTO.toEntity());
//
//            mockMvc.perform(get("/categories"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$", Matchers.hasSize(2)))
//                    .andExpect(jsonPath("$.[0].name").value("category"))
//                    .andExpect(jsonPath("$.[1].picture").value("picture"))
//                    .andDo(print());
//        }
//        @Test
//        @DisplayName("전체 category 가져올 때 없으면 빈 배열 리턴")
//        void getAllCategories_1() throws Exception {
//            mockMvc.perform(get("/categories"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$", Matchers.hasSize(0)))
//                    .andDo(print());
//        }
//        @Test
//        @DisplayName("category 하나 삭제 성공")
//        void deleteCategory() throws Exception {
//            String accessToken = jwtUtil.createAccessToken("jangtari", RoleType.ADMIN);
//            Cookie cookie = cookieUtil.createAccessCookie(accessToken);
//            CategoryDTO.Add addDTO = makeCategoryDTOwithoutPicture();
//            categoryRepository.save(addDTO.toEntity());
//            categoryRepository.save(addDTO.toEntity());
//
//            mockMvc.perform(delete("/admin/category/1").cookie(cookie))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$").value("OK"))
//                    .andDo(print());
//
//            mockMvc.perform(get("/categories"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$", Matchers.hasSize(1)))
//                    .andExpect(jsonPath("$.[0].name").value("category"))
//                    .andDo(print());
//        }
//    }
//    @Nested
//    @DisplayName("실패 테스트")
//    class FailureTest {
//        @Test
//        @DisplayName("권한 체크 실패")
//        void authCheck_X() throws Exception {
//            // addCategory
//            mockMvc.perform(multipart("/admin/category"))
//                    .andExpect(status().is4xxClientError())
//                    .andDo(print());
//            String accessToken = jwtUtil.createAccessToken("jangtari", RoleType.USER);
//            Cookie cookie = cookieUtil.createAccessCookie(accessToken);
//            mockMvc.perform(multipart("/admin/category").cookie(cookie))
//                    .andExpect(status().is4xxClientError())
//                    .andDo(print());
//
//            // updateCategory
//            mockMvc.perform(multipart("/admin/category/1"))
//                    .andExpect(status().is4xxClientError())
//                    .andDo(print());
//            accessToken = jwtUtil.createAccessToken("jangtari", RoleType.USER);
//            cookie = cookieUtil.createAccessCookie(accessToken);
//            mockMvc.perform(multipart("/admin/category1").cookie(cookie))
//                    .andExpect(status().is4xxClientError())
//                    .andDo(print());
//
//            // deleteCategory
//            mockMvc.perform(delete("/admin/category/1").cookie(cookie))
//                    .andExpect(status().is4xxClientError())
//                    .andDo(print());
//            accessToken = jwtUtil.createAccessToken("jangtari", RoleType.USER);
//            cookie = cookieUtil.createAccessCookie(accessToken);
//            mockMvc.perform(delete("/admin/category/1").cookie(cookie))
//                    .andExpect(status().is4xxClientError())
//                    .andDo(print());
//        }
//        @Test
//        @DisplayName("category 추가 실패 - name X")
//        void post_X1() throws Exception {
//            String accessToken = jwtUtil.createAccessToken("jangtari", RoleType.ADMIN);
//            Cookie cookie = cookieUtil.createAccessCookie(accessToken);
//            mockMvc.perform(multipart("/admin/category").param("name", "").cookie(cookie))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print());
//        }
//        @Test
//        @DisplayName("category 수정 실패 - name X")
//        void put_X1() throws Exception {
//            String accessToken = jwtUtil.createAccessToken("jangtari", RoleType.ADMIN);
//            Cookie cookie = cookieUtil.createAccessCookie(accessToken);
//            mockMvc.perform(multipart("/admin/category/1").param("name", "").cookie(cookie))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print());
//        }
//        @Test
//        @DisplayName("category 수정 실패 - NoSuchCategoryException")
//        void put_X2() throws Exception {
//            String accessToken = jwtUtil.createAccessToken("jangtari", RoleType.ADMIN);
//            Cookie cookie = cookieUtil.createAccessCookie(accessToken);
//            mockMvc.perform(multipart("/admin/category/1").param("name", "aa").cookie(cookie))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.message").value("No Such Category"))
//                    .andExpect(jsonPath("$.status").value(404))
//                    .andExpect(jsonPath("$.errors").doesNotExist())
//                    .andExpect(jsonPath("$.code").value("yu007"))
//                    .andDo(print());
//        }
//        @Test
//        @DisplayName("category 삭제 실패 - NoSuchCategoryException")
//        void delete_X2() throws Exception {
//            String accessToken = jwtUtil.createAccessToken("jangtari", RoleType.ADMIN);
//            Cookie cookie = cookieUtil.createAccessCookie(accessToken);
//            mockMvc.perform(delete("/admin/category/1").cookie(cookie))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.message").value("No Such Category"))
//                    .andExpect(jsonPath("$.status").value(404))
//                    .andExpect(jsonPath("$.errors").doesNotExist())
//                    .andExpect(jsonPath("$.code").value("yu007"))
//                    .andDo(print());
//        }
//    }
//
//    private CategoryDTO.Add makeCategoryDTOwithPicture() {
//        return CategoryDTO.Add.builder()
//                .name("category")
//                .picture(new MockMultipartFile("pic1", new byte[]{0}))
//                .build();
//    }
//    private CategoryDTO.Add makeCategoryDTOwithoutPicture() {
//        return CategoryDTO.Add.builder()
//                .name("category")
//                .build();
//    }
//    private CategoryDTO.Update makeUpdateCategoryDTOwithoutPicture() {
//        return CategoryDTO.Update.builder()
//                .name("updated")
//                .build();
//    }
//}
