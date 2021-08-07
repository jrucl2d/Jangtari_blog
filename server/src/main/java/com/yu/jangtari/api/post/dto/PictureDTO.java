package com.yu.jangtari.api.post.dto;

import com.yu.jangtari.api.picture.domain.Picture;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PictureDTO {
    private String picture;

    public PictureDTO(Picture picture) {
        this.picture = picture.getUrl();
    }
}
