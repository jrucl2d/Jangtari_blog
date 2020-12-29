package com.yu.jangtari.repository.post;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.yu.jangtari.domain.Category;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.domain.QCategory;
import com.yu.jangtari.domain.QPost;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository {

    public PostRepositoryImpl(){
        super(Post.class);
    }

    @Override
    public List<PostDTO.GetAll> getPostList(Long categoryId) {
        QCategory category = QCategory.category;
        JPQLQuery<Category> query1 = from(category);
        query1.where(category.id.eq(categoryId));
        Category foundCategory = query1.fetchOne();

        QPost post = QPost.post1;
        JPQLQuery<Post> query = from(post);
        JPQLQuery<Tuple> tuple = query.select(post.id, post.title);
        tuple.where(post.category.eq(foundCategory));
        List<Tuple> list = tuple.fetch();
        List<PostDTO.GetAll> resultList = new ArrayList<>();
        list.forEach(t -> {
            resultList.add(new PostDTO.GetAll((Long)t.toArray()[0], (String)t.toArray()[1]));
        });
        return resultList;
    }
}
