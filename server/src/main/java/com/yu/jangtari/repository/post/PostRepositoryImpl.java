package com.yu.jangtari.repository.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.domain.*;
import com.yu.jangtari.domain.DTO.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository {
    private final String TITLE = "t";
    private final String CONTENT = "c";
    private final String HASHTAG = "h";
    private final JPAQueryFactory jpaQueryFactory;

    public PostRepositoryImpl(JPAQueryFactory jpaQueryFactory){
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }


    // Post 정보와 함께 Comment, Picture, Hashtag 정보도 같이 리턴해야 함
    @Override
    public Optional<Post> getOne(Long postId) {
        QPost post = QPost.post;
        QComment comment = QComment.comment;
        QPicture picture = QPicture.picture;
        QPostHashtag postHashtag = QPostHashtag.postHashtag;
        final Post tmpPost = jpaQueryFactory.selectFrom(post)
                .leftJoin(post.comments, comment)
                .leftJoin(post.pictures, picture)
                .leftJoin(post.postHashtags, postHashtag)
                .where(post.id.eq(postId))
                .fetchOne();
        if (tmpPost == null || tmpPost.getDeleteFlag().isDeleteFlag()) return Optional.empty();
        return Optional.of(tmpPost);
    }
    @Override
    public List<Post> getPostListForDelete(Long categoryId) {
        QPost post = QPost.post;
        return jpaQueryFactory.selectFrom(post)
                .where(post.category.id.eq(categoryId))
                .leftJoin(post.comments)
                .leftJoin(post.pictures)
                .leftJoin(post.postHashtags)
                .fetch();
    }
    @Override
    public Page<PostDTO.Get> getPostList(Long categoryId, PageRequest pageRequest) {
        final Pageable pageable = pageRequest.of();
        final String type = pageRequest.getType();
        final String keyword = pageRequest.getKeyword();

        JPQLQuery<PostDTO.Get> query;
        QPost post = QPost.post;
        // select, from
        query = jpaQueryFactory.select(Projections.constructor(PostDTO.Get.class, post.id, post.title)).from(post);

        // where
        BooleanBuilder bb = new BooleanBuilder();
        if (type != null) setSearchCondition(post, bb, keyword, type);
        setCommonCondition(post, bb, categoryId);
        query.where(bb);

        final List<PostDTO.Get> posts = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(posts, pageable, query.fetchCount());
    }

    private void setSearchCondition(QPost post, BooleanBuilder bb, String keyword, String type) {
        if (type.equals(TITLE)) {
            bb.and(post.title.contains(keyword));
        }
        else if (type.equals(CONTENT)) {
            bb.and(post.content.contains(keyword));
        }
        else if (type.equals(HASHTAG)) {
            bb.and(post.postHashtags.any().hashtag.content.eq(keyword));
        }
    }
    private void setCommonCondition(QPost post, BooleanBuilder bb, Long categoryId) {
        bb.and(post.category.id.eq(categoryId));
        bb.and(post.deleteFlag.deleteFlag.isFalse());
    }
}
