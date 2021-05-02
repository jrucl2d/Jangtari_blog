package com.yu.jangtari.CategoryTest;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yu.jangtari.RepositoryTest;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.category.CategoryRepositoryQuerydsl;
import io.jsonwebtoken.lang.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class QuerydslTest {

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
