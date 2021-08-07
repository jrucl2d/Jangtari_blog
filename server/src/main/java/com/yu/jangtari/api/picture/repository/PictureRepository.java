package com.yu.jangtari.api.picture.repository;

import com.yu.jangtari.api.picture.domain.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, String> {
}
