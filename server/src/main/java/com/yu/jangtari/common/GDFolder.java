package com.yu.jangtari.common;

import lombok.Getter;

@Getter
public enum GDFolder {
    CATEGORY(0), JANGTARI(1), POST(2);
    private final int number;
    GDFolder(int number) {
        this.number = number;
    }
}
