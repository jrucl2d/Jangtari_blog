package com.yu.jangtari.PostTest;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.common.PageRequest;
import com.yu.jangtari.domain.DTO.CategoryDTO;
import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.domain.Post;
import com.yu.jangtari.repository.post.PostRepository;
import com.yu.jangtari.service.CategoryService;
import com.yu.jangtari.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;

public class PostPageTest extends IntegrationTest {
    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostRepository postRepository;

    @Test
    void test() {
        categoryService.addCategory(makeCategoryDTOwithoutPicture());
        postService.addPost(makeAddPostDTO());
        postService.addPost(makeAddPostDTO());
        postService.addPost(makeAddPostDTO());
        Page<Post> posts = postService.getPostList(1L, new PageRequest(1, null, "ten"));
        System.out.println("니ㅓ니나ㅓ니ㅏㅓ니ㅏㅓ니");
//        for (Post post : posts) {
//            System.out.println(post);
//        }
        System.out.println("거러거거ㅓ러걱");
    }
    private CategoryDTO.Add makeCategoryDTOwithoutPicture() {
        return CategoryDTO.Add.builder()
                .name("category")
                .build();
    }
    private PostDTO.Add makeAddPostDTO() {
        return PostDTO.Add.builder()
                .title("post title")
                .content("post content")
                .categoryId(1L)
                .template(1)
                .hashtags(Arrays.asList("aaa", "bbb", "ccc"))
                .build();
    }
}
