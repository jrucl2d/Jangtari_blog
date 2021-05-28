package com.yu.jangtari.repository.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository {
    private final String TITLE = "t";
    private final String CONTENT = "c";
    private final String HASHTAG = "h";
    private final JPAQueryFactory jpaQueryFactory;

    public PostRepositoryImpl(JPAQueryFactory jpaQueryFactory){
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    /**
     * Post 정보와 함께 Comment, Picture, Hashtag 정보도 같이 리턴해야 함
     */
    @Override
    public Optional<Post> getOne(Long postId) {
        QPost post = QPost.post;
        return Optional.ofNullable(jpaQueryFactory.selectFrom(post)
                .where(post.id.eq(postId))
                .leftJoin(post.comments)
                .leftJoin(post.pictures)
                .leftJoin(post.postHashtags)
                .fetchOne());
    }

//    @Override
//    public Page<Post> getPostList(Long categoryId, PageRequest pageRequest) {
//        final Pageable pageable = pageRequest.of();
//        String type = pageRequest.getType();
//        final String keyword = pageRequest.getType();
//        QPost post = QPost.post;
//        List<Post> fetch = jpaQueryFactory.selectFrom(post)
//                .where(post.category.id.eq(categoryId))
//                .where(post.content.containsIgnoreCase("ont"))
//                .where(post.deleteFlag.deleteFlag.isFalse()).fetch();
//        for (Post fetch1 : fetch) {
//            System.out.println(fetch1);
//        }
//        return null;
//    }

    @Override
    public Page<Post> getPostList(Long categoryId, PageRequest pageRequest) {
        if (pageRequest.getType() == null) pageRequest.setType("nothing");
        final Pageable pageable = pageRequest.of();
        final String type = pageRequest.getType();
        final String keyword = pageRequest.getKeyword();
        JPQLQuery<Post> query;
        if (type.equals(HASHTAG)) query = getPostListWithHashtag(categoryId, keyword);
        else {
            QPost post = QPost.post;
            query = jpaQueryFactory.selectFrom(post);
            BooleanBuilder bb = new BooleanBuilder();
            bb.and(post.category.id.eq(categoryId));
            if (type.equals(TITLE)) {
                bb.and(post.title.contains(keyword));
            }
            else if (type.equals(CONTENT)) {
                bb.and(post.content.contains(keyword));
            }
            bb.and(post.deleteFlag.deleteFlag.isFalse());
            query.where(bb);
        }
        final List<Post> posts = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(posts, pageable, query.fetchCount());
    }
    private JPQLQuery<Post> getPostListWithHashtag(Long categoryId, String keyword) {
        final QPostHashtag postHashtag = QPostHashtag.postHashtag;
        return jpaQueryFactory
                .select(postHashtag.post)
                .from(postHashtag)
                .where(postHashtag.hashtag.content.eq(keyword)
                .and(postHashtag.post.category.id.eq(categoryId))
                .and(postHashtag.post.deleteFlag.deleteFlag.isFalse()));
    }

//    @Override
//    public PageMakerVO<PostDTO.GetAll> getPostList(Long categoryId, PageVO pageVO, String type, String keyword) {
//        Pageable pageable = pageVO.makePageable("DESC", "createddate");
//
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
//
//        QPost post = QPost.post1;
//
//        JPAQuery<Tuple> tmp = queryFactory.select(post.id, post.title, post.template)
//                .from(post);
//        if(type != null){
//            if(type.equals("t")){
//                tmp.where(post.title.contains(keyword).and(post.category.id.eq(categoryId)));
//            }else if(type.equals("c")){
//                tmp.where(post.post.contains(keyword).and(post.category.id.eq(categoryId)));
//            } else if(type.equals("h")){
//                QHashtag hashtag = QHashtag.hashtag1;
//                tmp.where(post.category.id.eq(categoryId)).innerJoin(post.hashtags, hashtag).where(hashtag.hashtag.eq(keyword));
//            }
//        } else{
//            tmp.where(post.category.id.eq(categoryId));
//        }
//        tmp.offset(pageable.getOffset()).limit(pageable.getPageSize());
//        QueryResults<Tuple> results = tmp.fetchResults();
//        List<Tuple> list = results.getResults();
//
//        List<PostDTO.GetAll> resultList = new ArrayList<>();
//
//        list.forEach(t -> {
//            resultList.add(new PostDTO.GetAll((Long)t.toArray()[0], (String)t.toArray()[1], (Integer)t.toArray()[2]));
//        });
//        long totalCount = list.size();
//
//        int totalPageSize = (int)(Math.ceil(results.getTotal() / (double)pageVO.getSize()));
//
//        return new PageMakerVO<>(new PageImpl<>(resultList, pageable, totalCount), totalPageSize);
//    }
}
