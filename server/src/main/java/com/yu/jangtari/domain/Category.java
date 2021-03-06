package com.yu.jangtari.domain;

import com.yu.jangtari.domain.DTO.CategoryDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@Table(name = "category") // 클래스명 바뀔 경우의 영향 최소화
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of="id", callSuper = false)
public class Category extends DateAuditing {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String name;

    @Column(name = "category_picture")
    private String picture;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // soft delete 구현을 위해 REMOVE는 제외
    private List<Post> posts = new ArrayList<>(); // 비어있음을 판단할 때 null보다 isEmtpy가 더 직관적

    @Embedded
    private DeleteFlag deleteFlag;

    @Builder
    public Category(String name, String picture) {
        this.name = name;
        this.picture = picture;
        this.deleteFlag = DeleteFlag.initDeleteFlag();
    }
    public void updateCategory(CategoryDTO.Update categoryDTO, String pictureURL) {
        this.name = categoryDTO.getName();
        if (pictureURL != null) this.picture = pictureURL;
    }
}
