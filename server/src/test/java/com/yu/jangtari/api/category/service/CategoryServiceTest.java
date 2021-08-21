package com.yu.jangtari.api.category.service;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.category.dto.CategoryDto;
import com.yu.jangtari.api.category.repository.CategoryRepository;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.testHelper.MultipartUtil;
import com.yu.jangtari.util.GoogleDriveUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CategoryServiceTest extends ServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private GoogleDriveUtil googleDriveUtil;

    @Mock
    private CategoryRepository categoryRepository;

    private static Category category;

    @BeforeAll
    static void beforeAll()
    {
        category = Category.builder()
            .name("category")
            .picture("picture")
            .build();
    }

    @Test
    @DisplayName("cateogry를 저장하며 multipart file을 저장 후 가져온 url을 저장함")
    void addCategory()
    {
        // given
        CategoryDto.Add categoryDto = CategoryDto.Add.builder()
            .name("category name")
            .picture(MultipartUtil.createOne("category"))
            .build();
        given(googleDriveUtil.fileToURL(any(), any())).willReturn("category");

        // when
        categoryService.addCategory(categoryDto);

        // then
        verify(googleDriveUtil, times(1)).fileToURL(any(), any());
        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("category 정보를 수정하며 새로운 사진의 경우 url을 변경함")
    void updateCategory()
    {
        // given
        CategoryDto.Update categoryDto = CategoryDto.Update.builder()
            .name("new category name")
            .picture(MultipartUtil.createOne("category"))
            .build();
        given(googleDriveUtil.fileToURL(any(), any())).willReturn("category");
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        categoryService.updateCategory(1L, categoryDto);

        // then
        verify(googleDriveUtil, times(1)).fileToURL(any(), any());
    }

    @Test
    @DisplayName("category 정보를 수정하려 할 때 category 없으면 BusinessException 발생")
    void updateCategory_X()
    {
        // given
        CategoryDto.Update categoryDto = CategoryDto.Update.builder()
            .name("new category name")
            .picture(MultipartUtil.createOne("category"))
            .build();
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        BusinessException e = assertThrows(BusinessException.class,
            () -> categoryService.updateCategory(1L, categoryDto));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND_ERROR);
    }

    // TODO : post 이하 모든 도메인 삭체 처리 테스트 이후에 category 삭체 처리 구현 필요
    @Test
    @DisplayName("cateogry, 연관된 post 관련 정보 모두 삭제 처리")
    void deleteCategory()
    {
        // given

        // when

        // then
    }
}