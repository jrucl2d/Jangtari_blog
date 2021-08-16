package com.yu.jangtari.api.category.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yu.jangtari.api.category.domain.Category;
import com.yu.jangtari.api.category.domain.QCategory;
import com.yu.jangtari.common.QDeleteFlag;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

/**
 * QuerydslRepositorySupport는 페이징이 필요할 때만 있으면 된다.
 */
public class CategoryRepositoryImpl extends QuerydslRepositorySupport implements CustomCategoryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public CategoryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Category.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Category> getAllCategories() {
        QCategory category = QCategory.category;
        return jpaQueryFactory.selectFrom(category)
                .where(QDeleteFlag.deleteFlag.isDeleted)
                .orderBy(category.createdDate.asc())
                .fetch();
    }
}
