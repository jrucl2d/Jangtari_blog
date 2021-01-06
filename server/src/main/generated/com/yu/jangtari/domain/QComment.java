package com.yu.jangtari.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QComment is a Querydsl query type for Comment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QComment extends EntityPathBase<Comment> {

    private static final long serialVersionUID = -1475019414L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QComment comment1 = new QComment("comment1");

    public final StringPath comment = createString("comment");

    public final DateTimePath<java.sql.Timestamp> createddate = createDateTime("createddate", java.sql.Timestamp.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final QPost post;

    public final QComment recomment;

    public final ListPath<Comment, QComment> subcomment = this.<Comment, QComment>createList("subcomment", Comment.class, QComment.class, PathInits.DIRECT2);

    public QComment(String variable) {
        this(Comment.class, forVariable(variable), INITS);
    }

    public QComment(Path<? extends Comment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QComment(PathMetadata metadata, PathInits inits) {
        this(Comment.class, metadata, inits);
    }

    public QComment(Class<? extends Comment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
        this.recomment = inits.isInitialized("recomment") ? new QComment(forProperty("recomment"), inits.get("recomment")) : null;
    }

}

