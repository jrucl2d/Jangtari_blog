package com.yu.jangtari.common;

import lombok.*;

import javax.persistence.Embeddable;

// soft delete 구현을 위한 삭제 flag
@Getter
@Embeddable
@ToString
@EqualsAndHashCode(of="isDeleted")
public class DeleteFlag {
    private boolean isDeleted;

    public DeleteFlag() {
        this.isDeleted = false;
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
