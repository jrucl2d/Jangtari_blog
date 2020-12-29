package com.yu.jangtari.repository.category;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.domain.QCategory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepositoryImpl extends QuerydslRepositorySupport implements CustomCategoryRepository {

    public CategoryRepositoryImpl() {
        super(Category.class);
    }

    @Override
    public List<CategoryDTO.Get> getAllCategories() {

        QCategory category = QCategory.category;
        JPQLQuery<Category> query = from(category);

        JPQLQuery<Tuple> tuple = query.select(category.id, category.name, category.picture);
        tuple.orderBy(category.createddate.asc());
        List<Tuple> list = tuple.fetch();

        List<CategoryDTO.Get> result = new ArrayList<>();
        list.forEach(t -> {
            result.add(new CategoryDTO.Get((Long)t.toArray()[0], (String)t.toArray()[1], (String)t.toArray()[2]));
        });
        return result;
    }
}
