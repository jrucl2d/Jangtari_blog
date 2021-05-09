package com.yu.jangtari.CategoryTest;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.category.CategoryRepositoryQuerydsl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CategoryQueryDslTest extends IntegrationTest {

    @Autowired
    private CategoryRepositoryQuerydsl repository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Nested
    @DisplayName("성공 테스트")
    class SuccessTest {
        @Test
        @DisplayName("querydsl repository getAllCategories 성공")
        void getAllCategories_O() {
            // given
            List<Category> categories = makeCategories();
            // when
            categoryRepository.saveAll(categories);
            // then
            assertThat(repository.getAllCategories().size()).isEqualTo(10);
        }
    }
    @Nested
    @DisplayName("실패 테스트")
    class FailureTest {

    }

    private List<Category> makeCategories() {
        List<Category> categories = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> {
            categories.add(Category.builder().name("category " + i).picture("picture " + i).build());
        });
        return categories;
    }
}
