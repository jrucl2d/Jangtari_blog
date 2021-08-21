package com.yu.jangtari.api.post.domain;

import com.yu.jangtari.common.DateAuditing;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of="content", callSuper = false)
public class Hashtag extends DateAuditing
{
    @Id
    @Column(name = "hashtag")
    private String content;

    public Hashtag(String content) {
        this.content = content;
    }
}
