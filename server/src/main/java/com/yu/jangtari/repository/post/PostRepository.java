package com.yu.jangtari.repository.post;

import com.yu.jangtari.domain.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends CrudRepository<Post, Long>, CustomPostRepository {
    @Query("select p from Post p where p.category.id = :categoryId")
    List<Post> getPostListForDelete(@Param(value = "categoryId") Long categoryId);
}
