package com.yu.jangtari.api.category.domain;

import com.yu.jangtari.api.category.dto.CategoryDto;
import com.yu.jangtari.common.DateAuditing;
import com.yu.jangtari.common.DeleteFlag;
import com.yu.jangtari.api.post.domain.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@ToString
@Table(name = "category") // 클래스명 바뀔 경우의 영향 최소화
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends DateAuditing
{
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

    @Builder
    private Category(Long id, String name, String picture, List<Post> posts) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.deleteFlag = new DeleteFlag();
        this.posts = posts == null ? new ArrayList<>() : posts;
    }

    public void softDelete() {
        this.deleteFlag.softDelete();
        this.posts.forEach(Post::softDelete);
    }

    public Category updateCategory(CategoryDto.Update categoryDTO) {
        String pictureUrl = categoryDTO.getPictureUrl() == null ? this.picture : categoryDTO.getPictureUrl();
        return Category.builder()
            .id(categoryDTO.getCategoryId())
            .name(categoryDTO.getName())
            .picture(pictureUrl)
            .build();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id)
            && Objects.equals(name, category.name)
            && Objects.equals(picture, category.picture)
            && Objects.equals(posts, category.posts)
            && Objects.equals(deleteFlag, category.deleteFlag);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, picture, posts, deleteFlag);
    }
}
