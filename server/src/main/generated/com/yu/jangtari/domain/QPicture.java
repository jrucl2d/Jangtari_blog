package com.yu.jangtari.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPicture is a Querydsl query type for Picture
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPicture extends EntityPathBase<Picture> {

    private static final long serialVersionUID = 1291807753L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPicture picture1 = new QPicture("picture1");

    public final DateTimePath<java.sql.Timestamp> createddate = createDateTime("createddate", java.sql.Timestamp.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath picture = createString("picture");

    public final QPost post;

    public QPicture(String variable) {
        this(Picture.class, forVariable(variable), INITS);
    }

    public QPicture(Path<? extends Picture> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPicture(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPicture(PathMetadata metadata, PathInits inits) {
        this(Picture.class, metadata, inits);
    }

    public QPicture(Class<? extends Picture> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
    }

}

