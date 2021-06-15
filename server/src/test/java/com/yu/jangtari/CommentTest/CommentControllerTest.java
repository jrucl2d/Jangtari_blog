package com.yu.jangtari.CommentTest;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.repository.CommentRepository;
import com.yu.jangtari.repository.category.CategoryRepository;
import com.yu.jangtari.repository.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class CommentControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("getComments, 전체 comment 불러오기 성공")
    void getComments_O() {
        Category category = Category.builder().name("category").build();
        categoryRepository.save(category);
    }
}
