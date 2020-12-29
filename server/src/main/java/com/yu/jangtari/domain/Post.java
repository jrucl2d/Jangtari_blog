package com.yu.jangtari.domain;

import com.sun.istack.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "category")
@Entity
@EqualsAndHashCode(of="id")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @Lob
    private String post;

    private int template;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Picture> pictures = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "PostHashtag")
    private List<Hashtag> hashtags = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createddate;
}
