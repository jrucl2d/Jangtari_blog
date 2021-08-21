package com.yu.jangtari.api.category.repository;

import com.yu.jangtari.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategoryRepositoryImplTest extends IntegrationTest {

    @Autowired
    private CategoryRepositoryImpl categoryRepository;

    @Test
    @DisplayName("getCategoryForDelete 제대로 된 sql 실행되는지 확인")
    void getCategoryForDelete()
    {
        // given
        // when
        // then
        categoryRepository.findCategoryForDelete(1L);
    }
}