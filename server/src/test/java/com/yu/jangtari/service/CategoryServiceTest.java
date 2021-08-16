package com.yu.jangtari.service;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.api.category.service.CategoryService;
import com.yu.jangtari.common.exception.NoSuchCategoryException;
import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.category.dto.CategoryDTO;
import com.yu.jangtari.api.category.repository.CategoryRepository;
import com.yu.jangtari.api.post.service.PostService;
import com.yu.jangtari.util.GoogleDriveUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CategoryServiceTest extends ServiceTest {
    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private GoogleDriveUtil googleDriveUtil;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private PostService postService;

    @Test
    @DisplayName("저장되어 있는 모든 카테고리를 잘 가져옴")
    void getAllCategories()
    {
        // given
        given(categoryRepository.getAllCategories()).willReturn(Arrays.asList(
            Category.builder().name("1").build(),
            Category.builder().name("2").build())
        );

        // when
        List<Category> categories = categoryService.getAllCategories();

        // then
        assertEquals(categories.size(), 2);
    }

    @Test
    @DisplayName("category를 추가")
    void addCategory()
    {
        // given
        MockMultipartFile multipartFile = new MockMultipartFile("mock", new byte[] {});
        CategoryDTO.Add catDTO = CategoryDTO.Add.builder().name("name").picture(multipartFile).build();
        CategoryDTO.Add catDTOwithPic = CategoryDTO.Add.builder().name("name").build();
        catDTOwithPic.setPictureURL("picture");

        given(googleDriveUtil.fileToURL(any(), any())).willReturn("picture");
        given(categoryRepository.save(any())).willReturn(catDTOwithPic.toEntity());

        // when
        categoryService.addCategory(catDTO);

        // then
        verify(googleDriveUtil, times(1)).fileToURL(any(), any());
        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("사진은 제외하고 category를 수정")
    void updateCategory()
    {
        // given
        CategoryDTO.Update catDTO = CategoryDTO.Update.builder().name("updated").build();
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(Category.builder().name("category").picture("picture").build()));
        given(googleDriveUtil.fileToURL(any(), any())).willReturn(null);

        // when
        Category category = categoryService.updateCategory(1L, catDTO);

        // then
        verify(googleDriveUtil, times(1)).fileToURL(any(), any());
        assertEquals("updated", category.getName());
        assertEquals("picture", category.getPicture());
    }

    @Test
    @DisplayName("사진을 포함해 category를 수정")
    void updateCategory1()
    {
        // given
        CategoryDTO.Update catDTO = CategoryDTO.Update.builder().name("updated").build();
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(Category.builder().name("category").picture("picture").build()));
        given(googleDriveUtil.fileToURL(any(), any())).willReturn("newPicture");

        // when
        Category category = categoryService.updateCategory(1L, catDTO);

        // then
        verify(googleDriveUtil, times(1)).fileToURL(any(), any());
        assertEquals("updated", category.getName());
        assertEquals("newPicture", category.getPicture());
    }

    @Test
    @DisplayName("category를 삭제")
    void deleteCategory()
    {
        // given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(Category.builder().name("category").picture("picture").build()));
        doNothing().when(postService).deletePostsOfCategory(anyLong());

        // when
        Category category = categoryService.deleteCategory(1L);

        // then
        verify(postService, times(1)).deletePostsOfCategory(anyLong());
        assertTrue(category.getDeleteFlag().isDeleted());
    }

    @Test
    @DisplayName("id에 해당하는 category가 없으면 NoSuchCategoryException 발생")
    void findOne_O()
    {
        // given
        // when
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // then
        assertThrows(NoSuchCategoryException.class, () -> categoryService.findOne(1L));
    }
}