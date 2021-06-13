package com.yu.jangtari.domain;

import lombok.*;

import javax.persistence.Embeddable;

// soft delete 구현을 위한 삭제 flag
@Getter
@Embeddable
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of="deleteFlag")
public class DeleteFlag {
    private boolean deleteFlag;

    public DeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    // deleteflag는 최초에 false 상태로 초기화
    public static DeleteFlag initDeleteFlag() {
        return new DeleteFlag(false);
    }
    public void softDelete() {
        this.deleteFlag = true;
    }
}
