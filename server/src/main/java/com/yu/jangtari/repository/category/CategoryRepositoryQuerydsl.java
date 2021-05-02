package com.yu.jangtari.repository.category;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.QCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CategoryRepositoryQuerydsl {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Category> getAllCategories() {
        QCategory category = QCategory.category;
        return jpaQueryFactory.selectFrom(category)
                .orderBy(category.createdDate.asc())
                .fetch();
    }
}
