package com.yu.jangtari.domain;

import javax.persistence.Embeddable;

/**
 * soft delete을 구현하기 위한 삭제 flag
 */
@Embeddable
public class DeleteFlag {
    private boolean deleteFlag = false;

    public void softDelete() {
        this.deleteFlag = true;
    }
}
