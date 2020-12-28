package com.yu.jangtari.domain;

import com.sun.istack.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@EqualsAndHashCode(of="id")
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String hashtag;

    @CreationTimestamp
    private Timestamp createddate;

    private Timestamp deleteddate;

    @ManyToMany(mappedBy = "hashtags")
    private List<Post> posts;
}
