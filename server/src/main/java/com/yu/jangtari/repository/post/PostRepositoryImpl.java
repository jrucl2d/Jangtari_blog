package com.yu.jangtari.repository.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yu.jangtari.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class PostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository {

    private JPAQueryFactory jpaQueryFactory;

    public PostRepositoryImpl(JPAQueryFactory jpaQueryFactory){
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    /**
     * Post 정보와 함께 Comment, Picture, Hashtag 정보도 같이 리턴해야 함
     */
    @Override
    public Post getOne(Long postId) {
        QPost post = QPost.post;
        return jpaQueryFactory.selectFrom(post)
                .where(post.id.eq(postId))
                .leftJoin(post.comments)
                .leftJoin(post.pictures)
                .leftJoin(post.postHashtags)
                .fetchOne();
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
//
//    @Override
//    public List<CommentDTO.Get> getCommentsOfPost(Long postId) {
//
//        QComment comment = QComment.comment1;
//        JPQLQuery<Comment> query1 = from(comment);
//        JPQLQuery<Tuple> tuple1 = query1.select(comment.id, comment.comment, comment.member.username, comment.member.nickname, comment.recomment.id);
//        tuple1.where(comment.post.id.eq(postId)).orderBy(comment.recomment.id.asc()).orderBy(comment.createddate.asc());
//        List<Tuple> list1 = tuple1.fetch();
//
//        List<CommentDTO.Get> comments = new ArrayList<>();
//        list1.forEach(v -> comments.add(new CommentDTO.Get((Long)v.toArray()[0], (String)v.toArray()[1], (String)v.toArray()[2], (String)v.toArray()[3], (Long)v.toArray()[4])));
//
//        return comments;
//    }
//
//    @Override
//    public PostDTO.GetOne getPost(Long postId) {
//        PostDTO.GetOne result = new PostDTO.GetOne();
//
//        // 게시글 + 사진
//        QPost post = QPost.post1;
//        QPicture picture = QPicture.picture1;
//        JPQLQuery<Post> query = from(post);
//        JPQLQuery<Tuple> tuple = query.select(post.id, post.title, post.post, picture.picture, picture.id);
//        tuple.where(post.id.eq(postId));
//        tuple.leftJoin(post.pictures, picture);
//        List<Tuple> list = tuple.fetch();
//
//        query = from(post);
//        QHashtag hashtag = QHashtag.hashtag1;
//        JPQLQuery<String> hashtagTuple = query.select(hashtag.hashtag).distinct().innerJoin(post.hashtags, hashtag).where(post.id.eq(postId));
//        List<String> hashtagLists = hashtagTuple.fetch();
//        List<HashtagDTO> hashtags = new ArrayList<>();
//        hashtagLists.forEach(v -> hashtags.add(new HashtagDTO(v)));
//        result.setHashtags(hashtags);
//
//        result.setId((Long)list.get(0).toArray()[0]);
//        result.setTitle((String)list.get(0).toArray()[1]);
//        result.setContent((String)list.get(0).toArray()[2]);
//
//        List<PictureDTO> pictures = new ArrayList<>();
//        list.forEach(v -> pictures.add(new PictureDTO((Long)v.toArray()[4], (String)v.toArray()[3])));
//        result.setPictures(pictures);
//
//        // 댓글
//        QComment comment = QComment.comment1;
//        JPQLQuery<Comment> query1 = from(comment);
//        JPQLQuery<Tuple> tuple1 = query1.select(comment.id, comment.comment, comment.member.username, comment.member.nickname, comment.recomment.id);
//        tuple1.where(comment.post.id.eq(postId)).orderBy(comment.recomment.id.asc()).orderBy(comment.createddate.asc());
//        List<Tuple> list1 = tuple1.fetch();
//
//        List<CommentDTO.Get> comments = new ArrayList<>();
//        list1.forEach(v -> comments.add(new CommentDTO.Get((Long)v.toArray()[0], (String)v.toArray()[1], (String)v.toArray()[2], (String)v.toArray()[3], (Long)v.toArray()[4])));
//        result.setComments(comments);
//
//        return result;
//    }
//
//    @Override
//    public List<Hashtag> getHashtags(List<String> hashtags) {
//        QHashtag hashtag = QHashtag.hashtag1;
//        JPQLQuery<Hashtag> query = from(hashtag);
//        query.where(hashtag.hashtag.in(hashtags));
//        return query.fetch();
//    }
}
