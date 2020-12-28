package com.yu.jangtari.domain;

import com.sun.istack.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@ToString(exclude = "post")
@Entity
@EqualsAndHashCode(of="id")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String picture;

    @CreationTimestamp
    private Timestamp createddate;

    private Timestamp deleteddate;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
