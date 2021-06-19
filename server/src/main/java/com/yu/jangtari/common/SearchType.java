package com.yu.jangtari.common;

import com.yu.jangtari.common.exception.NoSearchtypeException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SearchType {
    TITLE('t'), CONTENT('c'), HASHTAG('h');
    private final char value;

    public static SearchType of(String type) {
        switch (type.charAt(0)) {
            case 't':
                return TITLE;
            case 'c':
                return CONTENT;
            case 'h':
                return HASHTAG;
            default:
                throw new NoSearchtypeException();
        }
    }
}
