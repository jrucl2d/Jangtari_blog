package com.yu.jangtari.repository.category;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.QCategory;
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
                .where(category.deleteFlag.deleteFlag.isFalse())
                .orderBy(category.createdDate.asc())
                .fetch();
    }
}
