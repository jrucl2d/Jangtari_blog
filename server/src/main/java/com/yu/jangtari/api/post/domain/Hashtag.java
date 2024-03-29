package com.yu.jangtari.api.post.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Entity
@Table(name = "hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag
{
    @Id
    @Column(name = "hashtag")
    private String content;

    public Hashtag(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
