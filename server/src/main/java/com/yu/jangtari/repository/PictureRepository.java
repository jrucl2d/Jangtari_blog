package com.yu.jangtari.repository;

import com.yu.jangtari.domain.Picture;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PictureRepository extends CrudRepository<Picture, Long> {

    @Modifying
    @Query("DELETE FROM Picture p where p.id in :postIds")
    public void deleteByIds(@Param("postIds")List<Long> postIds);
}
