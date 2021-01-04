package com.yu.jangtari.repository.post;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yu.jangtari.vo.PageMakerVO;
import com.yu.jangtari.vo.PageVO;
import com.yu.jangtari.domain.*;
import com.yu.jangtari.domain.DTO.CommentDTO;
import com.yu.jangtari.domain.DTO.PictureDTO;
import com.yu.jangtari.domain.DTO.PostDTO;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository {

    @PersistenceContext
    EntityManager em;

    public PostRepositoryImpl(){
        super(Post.class);
    }

    @Override
    public PageMakerVO<PostDTO.GetAll> getPostList(Long categoryId, PageVO pageVO, String type, String keyword) {
        Pageable pageable = pageVO.makePageable("DESC", "createddate");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QPost post = QPost.post1;

        JPAQuery<Tuple> tmp = queryFactory.select(post.id, post.title, post.template)
                .from(post);
        if(type != null){
            if(type.equals("t")){
                tmp.where(post.title.contains(keyword).and(post.category.id.eq(categoryId)));
            }else if(type.equals("c")){
                tmp.where(post.post.contains(keyword).and(post.category.id.eq(categoryId)));
            }
        } else{
            tmp.where(post.category.id.eq(categoryId));
        }
        tmp.offset(pageable.getOffset()).limit(pageable.getPageSize());
        QueryResults<Tuple> results = tmp.fetchResults();
        List<Tuple> list = results.getResults();

        List<PostDTO.GetAll> resultList = new ArrayList<>();

        list.forEach(t -> {
            resultList.add(new PostDTO.GetAll((Long)t.toArray()[0], (String)t.toArray()[1], (Integer)t.toArray()[2]));
        });
        long totalCount = list.size();

        int totalPageSize = (int)(Math.ceil(results.getTotal() / (double)pageVO.getSize()));

        return new PageMakerVO<>(new PageImpl<>(resultList, pageable, totalCount), totalPageSize);
    }

    @Override
    public PostDTO.GetOne getPost(Long postId) {
        PostDTO.GetOne result = new PostDTO.GetOne();

        // 게시글 + 사진
        QPost post = QPost.post1;
        QPicture picture = QPicture.picture1;
        JPQLQuery<Post> query = from(post);
        JPQLQuery<Tuple> tuple = query.select(post.id, post.title, picture.picture);
        tuple.leftJoin(post.pictures, picture);
        List<Tuple> list = tuple.fetch();

        result.setId((Long)list.get(0).toArray()[0]);
        result.setTitle((String)list.get(0).toArray()[1]);

        List<PictureDTO> pictures = new ArrayList<>();
        list.forEach(v -> pictures.add(new PictureDTO((String)v.toArray()[2])));
        result.setPictures(pictures);

        // 댓글
        QComment comment = QComment.comment1;
        JPQLQuery<Comment> query1 = from(comment);
        JPQLQuery<Tuple> tuple1 = query1.select(comment.id, comment.comment, comment.member.id, comment.recomment);
        tuple1.where(comment.post.id.eq(postId));
        List<Tuple> list1 = tuple1.fetch();

        List<CommentDTO.Get> comments = new ArrayList<>();
        list1.forEach(v -> comments.add(new CommentDTO.Get((Long)v.toArray()[0], (String)v.toArray()[1], (Long)v.toArray()[2], (String)v.toArray()[3])));
        result.setComments(comments);

        return result;
    }

    @Override
    public List<Hashtag> getHashtags(List<String> hashtags) {
        QHashtag hashtag = QHashtag.hashtag1;
        JPQLQuery<Hashtag> query = from(hashtag);
        query.where(hashtag.hashtag.in(hashtags));
        return query.fetch();
    }
}
