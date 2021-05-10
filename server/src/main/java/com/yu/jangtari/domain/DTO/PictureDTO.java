package com.yu.jangtari.domain.DTO;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PictureDTO {
    private Long id;
    private String picture;

    @Builder
    public PictureDTO(Long id, String picture) {
        this.id = id;
        this.picture = picture;
    }
}
