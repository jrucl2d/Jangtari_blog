package com.yu.jangtari.domain.DTO;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PictureDTO {
    private String picture;

    @Builder
    public PictureDTO(Long id, String picture) {
        this.picture = picture;
    }
}
