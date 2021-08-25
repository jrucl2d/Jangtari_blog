package com.yu.jangtari.api.post.repository.hashtag;

import com.yu.jangtari.api.post.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, String>
{
}
