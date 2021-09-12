package com.yu.jangtari.api.post.repository.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yu.jangtari.api.comment.domain.QComment;
import com.yu.jangtari.api.post.domain.Post;
import com.yu.jangtari.api.post.domain.QPicture;
import com.yu.jangtari.api.post.domain.QPost;
import com.yu.jangtari.api.post.domain.QPostHashtag;
import com.yu.jangtari.api.post.dto.PostDto;
import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.common.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public PostRepositoryImpl(JPAQueryFactory jpaQueryFactory){
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    // TODO : 여기 이후 sout tmpPost 할 때 N+1 문제 발생
    // Post 정보와 함께 Comment, Picture, Hashtag 정보도 같이 리턴해야 함
    @Override
    public Optional<Post> findJoining(Long postId) {
        QPost post = QPost.post;
//        QComment comment = QComment.comment;
//        QPicture picture = QPicture.picture;
//        QPostHashtag postHashtag = QPostHashtag.postHashtag;
        Post tmpPost = jpaQueryFactory.selectFrom(post)
//                .leftJoin(post.comments, comment).on(comment.deleteFlag.isDeleted.isFalse())
                .leftJoin(post.pictures).on(post.pictures.any().deleteFlag.isDeleted.isFalse())
//                .leftJoin(post.postHashtags, postHashtag).on(postHashtag.deleteFlag.isDeleted.isFalse())
                .where(post.id.eq(postId).and(post.deleteFlag.isDeleted.isFalse()))
                .fetchOne();
        System.out.println("여기");
        System.out.println(tmpPost);
        return Optional.ofNullable(tmpPost);
    }

    // 존재하지 않는 type으로 검색할 시 SearchType enum 내에서 Search Type Error 발생시킴
    @Override
    public Page<PostDto.ListGetElement> findPostList(Long categoryId, PageRequest pageRequest) {
        Pageable pageable = pageRequest.of();
        String type = pageRequest.getType();
        String keyword = pageRequest.getKeyword();

        JPQLQuery<PostDto.ListGetElement> query;
        QPost post = QPost.post;
        // select, from
        query = jpaQueryFactory.select(Projections.constructor(PostDto.ListGetElement.class, post.id, post.title)).from(post);

        // where
        BooleanBuilder bb = new BooleanBuilder();
        if (type != null)
            setSearchCondition(post, bb, keyword, type);
        bb.and(post.category.id.eq(categoryId));
        bb.and(post.deleteFlag.isDeleted.isFalse());

        query.where(bb);

        List<PostDto.ListGetElement> posts = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();

        long totalCount = pageRequest.getTotalCount() == null ? query.fetchCount() : pageRequest.getTotalCount();
        return new PageImpl<>(posts, pageable, totalCount);
    }

    private void setSearchCondition(QPost post, BooleanBuilder bb, String keyword, String type) {
        if (SearchType.of(type) == SearchType.TITLE) {
            bb.and(post.title.contains(keyword));
        }
        if (SearchType.of(type) == SearchType.CONTENT) {
            bb.and(post.content.contains(keyword));
        }
        if (SearchType.of(type) == SearchType.HASHTAG) {
            bb.and(post.postHashtags.any().hashtag.content.eq(keyword));
        }
    }
}
