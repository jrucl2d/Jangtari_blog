package com.yu.jangtari.repository.post;

import com.yu.jangtari.domain.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends CrudRepository<Post, Long>, CustomPostRepository{

    @Modifying
    @Query(value = "delete from hashtag where hashtag.id not in (select hashtag_id from post_hashtag)", nativeQuery = true)
    public int deleteTrashHashtags();

    @Modifying
    @Query(value="delete from post_hashtag where post_id=?1", nativeQuery = true)
    public int deleteBeforeHashtags(Long postId);
}
