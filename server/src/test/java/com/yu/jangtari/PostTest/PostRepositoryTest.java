package com.yu.jangtari.PostTest;

import com.yu.jangtari.RepositoryTest;
import com.yu.jangtari.repository.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PostRepositoryTest extends RepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Nested
    @DisplayName("성공 테스트")
    class SuccessTest {
        @Test
        @DisplayName("getOne 테스트 성공")
        void getOne_O() {
        }
    }
    @Nested
    @DisplayName("실패 테스트")
    class FailureTest {

    }
}
