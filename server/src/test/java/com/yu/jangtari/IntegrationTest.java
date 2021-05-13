package com.yu.jangtari;

import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// 서블릿 컨테이너를 모킹
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
public class IntegrationTest {
    @PersistenceContext
    EntityManager em;

    @AfterEach
    void afterEach() {
        em.createNativeQuery("alter table post alter column post_id restart with 1").executeUpdate();
        em.createNativeQuery("alter table comment alter column comment_id restart with 1").executeUpdate();
        em.createNativeQuery("alter table member alter column member_id restart with 1").executeUpdate();
        em.createNativeQuery("alter table category alter column category_id restart with 1").executeUpdate();
        em.createNativeQuery("alter table picture alter column picture_id restart with 1").executeUpdate();
        em.createNativeQuery("alter table post_hashtag alter column post_hashtag_id restart with 1").executeUpdate();
    }
}
