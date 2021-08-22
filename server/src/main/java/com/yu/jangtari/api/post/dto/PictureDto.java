package com.yu.jangtari.api.post.dto;

import com.yu.jangtari.api.post.domain.Picture;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PictureDto
{
    private String picture;

    public PictureDto(Picture picture) {
        this.picture = picture.getUrl();
    }
}
