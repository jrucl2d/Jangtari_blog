package com.yu.jangtari.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/*
 * extends, implement, Impl 없이 Querydsl을 사용할 수 있는 방법
 * JPAQueryFactory 빈을 컨테이너에 등록하여 프로젝트 전역에서 주입 받아 사용 가능
 * 출처 : https://jojoldu.tistory.com/372
 */
@Configuration
public class QuerydslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
