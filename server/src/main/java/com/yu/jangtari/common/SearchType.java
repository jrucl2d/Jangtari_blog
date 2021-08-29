package com.yu.jangtari.common;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SearchType {
    TITLE('t'), CONTENT('c'), HASHTAG('h');
    private final char type;

    public static SearchType of(String type) {
        switch (type.charAt(0)) {
            case 't':
                return TITLE;
            case 'c':
                return CONTENT;
            case 'h':
                return HASHTAG;
            default:
                throw new BusinessException(ErrorCode.NO_SEARCH_TYPE_ERROR);
        }
    }
}
