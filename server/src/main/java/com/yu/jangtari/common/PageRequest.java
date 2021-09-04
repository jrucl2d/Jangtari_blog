package com.yu.jangtari.common;

import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


/**
 * size 10 고정, direction createdDate DESC 고정
 * 클라이언트가 totalCount 안다면 보냄
*/
@Getter
public final class PageRequest {
    private static final int DEFAULT_SIZE = 10;
    private final int page;
    private final String type;
    private final String keyword;
    private final Long totalCount;

    public PageRequest(Integer page, String type, String keyword, Long totalCount) {
        this.page = (page == null || page <= 0) ? 0 : page - 1;
        this.type = type;
        this.keyword = keyword == null ? "" : keyword;
        this.totalCount = totalCount;
    }
    public Pageable of() {
        return org.springframework.data.domain.PageRequest.of(page, DEFAULT_SIZE, Sort.Direction.ASC, "createdDate");
    }
}
