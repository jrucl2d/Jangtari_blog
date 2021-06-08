package com.yu.jangtari.domain.DTO;

import com.yu.jangtari.domain.Picture;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PictureDTO {
    private String picture;

    public PictureDTO(Picture picture) {
        this.picture = picture.getUrl();
    }
}
