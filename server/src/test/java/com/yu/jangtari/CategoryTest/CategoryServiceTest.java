package com.yu.jangtari.CategoryTest;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.common.exception.FileTaskException;
import com.yu.jangtari.common.exception.GoogleDriveException;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.category.CategoryRepositoryQuerydsl;
import com.yu.jangtari.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class CategoryServiceTest extends ServiceTest {
    @InjectMocks
    private CategoryService categoryService;
    @Mock
    GoogleDriveUtil googleDriveUtil;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    CategoryRepositoryQuerydsl repositoryQuerydsl;

    @Nested
    @DisplayName("성공 테스트")
    class SuccessTest {
        @Test
        @DisplayName("addCategory - picture X, 성공")
        void addCategory_without_picture_O() throws GeneralSecurityException, IOException {
            // given
            CategoryDTO.Add categoryDTO = makeCategoryDTOwithoutPicture();
            Category category = categoryDTO.toEntity(null);
            given(categoryRepository.save(any())).willReturn(category);
            // when
            Category savedCategory = categoryService.addCategory(categoryDTO);
            // then
            assertThat(savedCategory.getName()).isEqualTo(categoryDTO.getName());
            assertThat(savedCategory.getPicture()).isNull();
            assertThat(savedCategory.getDeleteFlag().isDeleteFlag()).isFalse();
        }
        @Test
        @DisplayName("addCategory - picture O, 성공")
        void addCategory_with_picture_O() throws GeneralSecurityException, IOException {
            // given
            CategoryDTO.Add categoryDTO = makeCategoryDTOwithPicture();
            Category category = categoryDTO.toEntity("pic1");
            given(googleDriveUtil.fileToURL(any(), any())).willReturn(Arrays.asList("pic1"));
            given(categoryRepository.save(any())).willReturn(category);
            // when
            Category savedCategory = categoryService.addCategory(categoryDTO);
            // then
            assertThat(savedCategory.getName()).isEqualTo(categoryDTO.getName());
            assertThat(savedCategory.getPicture()).isEqualTo(categoryDTO.getPicture().get(0).getName());
            assertThat(savedCategory.getDeleteFlag().isDeleteFlag()).isFalse();
        }
        @Test
        @DisplayName("getAllCategories 성공")
        void getAllCategories_O() {
            // given
            CategoryDTO.Add categoryDTO = makeCategoryDTOwithoutPicture();
            Category category = categoryDTO.toEntity("pic1");
            given(categoryRepository.save(any())).willReturn(category);
            given(repositoryQuerydsl.getAllCategories()).willReturn(Arrays.asList(category, category, category));
            // when
            categoryService.addCategory(categoryDTO);
            categoryService.addCategory(categoryDTO);
            categoryService.addCategory(categoryDTO);
            // then
            List<Category> categories = categoryService.getAllCategories();
            assertThat(categories.size()).isEqualTo(3);
        }
    }
    @Nested
    @DisplayName("실패 테스트")
    class FailureTest {
        @Test
        @DisplayName("addCategory addCategory에서 GoogleDriveException 발생")
        void addCategory_Google_Drive_Exception_X() throws GeneralSecurityException, IOException {
            // given
            CategoryDTO.Add categoryDTO = makeCategoryDTOwithPicture();
            given(googleDriveUtil.fileToURL(any(), any())).willThrow(new GeneralSecurityException());
            // when, then
            assertThrows(GoogleDriveException.class, () -> categoryService.addCategory(categoryDTO));
        }
        @Test
        @DisplayName("addCategory addCategory에서 FileTaskException 발생")
        void addCategory_File_Task_Exception_O() throws GeneralSecurityException, IOException {
            // given
            CategoryDTO.Add categoryDTO = makeCategoryDTOwithPicture();
            given(googleDriveUtil.fileToURL(any(), any())).willThrow(new IOException());
            // when, then
            assertThrows(FileTaskException.class, () -> categoryService.addCategory(categoryDTO));
        }
    }

    private CategoryDTO.Add makeCategoryDTOwithPicture() {
        return CategoryDTO.Add.builder()
                .name("category")
                .multipartFiles(Arrays.asList(new MockMultipartFile("pic1", new byte[]{0})))
                .build();
    }
    private CategoryDTO.Add makeCategoryDTOwithoutPicture() {
        return CategoryDTO.Add.builder()
                .name("category")
                .multipartFiles(Arrays.asList())
                .build();
    }
}
