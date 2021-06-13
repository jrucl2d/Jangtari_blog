package com.yu.jangtari.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Embeddable;

/**
 * soft delete을 구현하기 위한 삭제 flag
 */
@Getter
@Embeddable
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteFlag {

    private boolean deleteFlag;

    // deleteFlag는 무조건 flase인 상태로 초기화되어야 함
    public static DeleteFlag initDeleteFlag() {
        DeleteFlag deleteFlag = new DeleteFlag();
        deleteFlag.deleteFlag = false;
        return deleteFlag;
    }
    public void softDelete() {
        this.deleteFlag = true;
    }
}
