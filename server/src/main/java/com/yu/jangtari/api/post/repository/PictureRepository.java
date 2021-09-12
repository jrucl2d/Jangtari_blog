package com.yu.jangtari.api.post.repository;

import com.yu.jangtari.api.post.domain.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, String> {
}
