package com.yu.jangtari.domain;

import lombok.*;
import javax.persistence.*;

@Getter
@ToString(exclude = "post")
@Entity
@Table(name = "picture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of="id", callSuper = false)
public class Picture extends DateAuditing {
    @Id
    @Column(name = "picture")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Picture(String url, Post post) {
        this.url = url;
        this.post = post;
    }
}
