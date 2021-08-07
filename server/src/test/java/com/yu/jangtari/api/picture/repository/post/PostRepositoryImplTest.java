package com.yu.jangtari.api.picture.repository.post;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PostRepositoryImplTest extends IntegrationTest {

    @Autowired private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder().name("category").build();
        categoryRepository.save(category);
    }

    // TODO : Post 손쉽게 Insert하는 Service 코드 테스트 완료 후에 작성 필요
    @Test
    @DisplayName("Post 정보와 함께 Comment, Picture, Hashtag도 가져오는지 확인")
    void getOne()
    {
        // given

        // when

        // then
    }
}