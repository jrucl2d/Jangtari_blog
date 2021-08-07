package com.yu.jangtari.api.category.repository.category;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.category.repository.CategoryRepository;
import com.yu.jangtari.api.category.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryImplTest extends IntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("deleteFlag가 true인 경우를 제외하고 Category들을 잘 가져옴")
    void getAllCategories()
    {
        // given
        List<Category> categories = Arrays.asList(
            Category.builder().name("name1").picture("picture1").build(),
            Category.builder().name("name2").picture("picture2").build(),
            Category.builder().name("name3").picture("picture3").build()
        );
        categories.get(1).getDeleteFlag().softDelete();
        categoryRepository.saveAll(categories);

        // when
        List<Category> gotCategories = categoryRepository.getAllCategories();

        // then
        assertEquals(2, gotCategories.size());
        assertEquals(gotCategories.get(0).getName(), categories.get(0).getName());
        assertEquals(gotCategories.get(1).getName(), categories.get(2).getName());
    }
}