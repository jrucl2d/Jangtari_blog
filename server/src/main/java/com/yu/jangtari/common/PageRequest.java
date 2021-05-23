package com.yu.jangtari.common;

import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


// size 10 고정, direction createdDate DESC 고정
@Getter
public final class PageRequest {
    private final int DEFAULT_SIZE = 10;
    private int page;
    private String type;
    private String keyword;

    public PageRequest(int page, String type, String keyword) {
        this.page = page;
        this.type = type;
        this.keyword = keyword;
    }
    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public Pageable of() {
        return org.springframework.data.domain.PageRequest.of(page, DEFAULT_SIZE, Sort.Direction.DESC, "createdDate");
    }
}
