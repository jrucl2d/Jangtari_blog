package com.yu.jangtari.api.post.controller;

import com.yu.jangtari.IntegrationTest;
import com.yu.jangtari.api.category.dto.CategoryDto;
import com.yu.jangtari.api.category.service.CategoryService;
import com.yu.jangtari.api.comment.dto.CommentDto;
import com.yu.jangtari.api.comment.service.CommentService;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.service.MemberService;
import com.yu.jangtari.api.post.domain.Picture;
import com.yu.jangtari.api.post.domain.Post;
import com.yu.jangtari.api.post.dto.PostDto;
import com.yu.jangtari.api.post.repository.PictureRepository;
import com.yu.jangtari.api.post.repository.hashtag.PostHashtagRepository;
import com.yu.jangtari.api.post.service.PostService;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.security.jwt.JwtInfo;
import com.yu.jangtari.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends IntegrationTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    private static String accessToken;
    private CategoryDto.Get category;
    private List<PostDto.ListGetElement> posts;
    private CommentDto.Get childComment;
    private Picture picture;
    private Picture deletePicture;

    @BeforeEach
    void setUp() {
        memberService.join(MemberDto.Add.builder()
            .username("jangtari")
            .nickname("nickname")
            .password("password")
            .build());
        memberService.getOne("jangtari");
        JwtInfo jwtInfo = JwtInfo.builder()
            .username("jangtari")
            .nickName("nickname")
            .roleType(RoleType.ADMIN)
            .build();
        accessToken = JwtUtil.createAccessToken(jwtInfo);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(jwtInfo, null, null)
        );

        category = categoryService.addCategory(CategoryDto.Add.builder()
            .name("category")
            .build(), null);

        posts = new ArrayList<>();
        IntStream.rangeClosed(1, 5).forEach(i ->
            posts.add(
                postService.addPost(
                    PostDto.Add.builder()
                        .title("title" + i)
                        .categoryId(category.getCategoryId())
                        .template(i % 2)
                        .content("content" + i)
                        .hashtags(
                            Arrays.asList("hashtag1", "hashtag2")
                        )
                        .build()
                    , null
                )
            )
        );

        CommentDto.Get parentComment = commentService.addComment(CommentDto.Add.builder()
                .postId(posts.get(0).getPostId())
                .content("comment1")
                .build());

        childComment = commentService.addComment(CommentDto.Add.builder()
            .postId(posts.get(0).getPostId())
            .content("comment1")
            .parentCommentId(parentComment.getCommentId())
            .build());

        picture = pictureRepository.save(Picture.builder().post(Post.builder().id(posts.get(0).getPostId()).build()).url("url").build());
        deletePicture = pictureRepository.save(Picture.builder().post(Post.builder().id(posts.get(0).getPostId()).build()).url("url2").build());
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("정상적으로 post 추가")
    void addPost() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("categoryId", String.valueOf(category.getCategoryId()));
        params.add("title", "postTitle");
        params.add("content", "postContent");

        mockMvc.perform(post("/admin/post")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(params))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("postTitle"))
            .andDo(print());
    }

    @Test
    @DisplayName("categoryId에 맞는 category 없다면 CATEGORY_NOT_FOUND_ERROR 발생")
    void addPost_X() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("categoryId", "123123123123123");
        params.add("title", "postTitle");
        params.add("content", "postContent");

        mockMvc.perform(post("/admin/post")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(params))
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.code").value(ErrorCode.CATEGORY_NOT_FOUND_ERROR.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.CATEGORY_NOT_FOUND_ERROR.getMessage()))
            .andDo(print());
    }

    @Test
    @DisplayName("정상적으로 post 수정")
    void updatePost() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("postId", String.valueOf(posts.get(0).getPostId()));
        params.add("title", "newTitle");
        params.add("content", "newContent");
        params.add("template", "0");
        List<String> hashtags = Arrays.asList("hashtag2", "hashtag3");
        String hashtagsString = objectMapper.writeValueAsString(hashtags);
        params.add("hashtags", hashtagsString);

        mockMvc.perform(put("/admin/post")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(params))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("newTitle"))
            .andExpect(jsonPath("$.content").value("newContent"))
            .andDo(print());
    }

    @ParameterizedTest(name = "[{index}] {5}")
    @CsvSource({
        ", , , , 5, 아무 조건 없으면 전체를 페이징하여 검색",
        ", 5, , , 5, totalCount 안다면 파라미터에 넣어서 보내고 count 쿼리 동작하지 않음",
        "2, , , , 0, 페이지에 아무 게시글도 없는 경우에는 빈 결과",
        ", , t, title1, 1, 제목으로 검색",
        ", , c, content2, 1, 내용으로 검색",
        ", , h, hashtag2, 5, 해시태그로 검색"
    })
    @DisplayName("categoryId에 해당하는 post 목록을 paging 하여 불러옴")
    void getPostList(String page, String totalCount, String type, String keyword, int result, String display) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (page != null)
            params.add("page", page);
        if (totalCount != null)
            params.add("totalCount", totalCount);
        if (type != null)
            params.add("type", type);
        if (keyword != null)
            params.add("keyword", keyword);

        mockMvc.perform(get("/category/" + category.getCategoryId() + "/posts")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .params(params))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(result)))
            .andDo(print());
    }

    @Test
    @DisplayName("정상적으로 post 를 연관관계 joining 해서 가져옴")
    void getPost() throws Exception {
        PostDto.ListGetElement post = posts.get(0);
        mockMvc.perform(get("/post/" + post.getPostId())
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getPostId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.comments", hasSize(2)))
                .andExpect(jsonPath("$.pictures", hasSize(2)))
                .andExpect(jsonPath("$.hashtags", hasSize(2)))
                .andExpect(jsonPath("$.pictures.[0].picture").value(picture.getUrl()))
                .andDo(print());
    }

    @Test
    @DisplayName("delete 한 내용들은 불러오지 않는다.")
    void getPost1() throws Exception {
        deletePicture.softDelete();
        commentService.deleteComment(childComment.getCommentId());
        postHashtagRepository.findAll().get(0).softDelete();

        entityManager.flush();
        entityManager.clear();

        PostDto.ListGetElement post = posts.get(0);
        mockMvc.perform(get("/post/" + post.getPostId())
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getPostId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.comments", hasSize(1)))
                .andExpect(jsonPath("$.hashtags", hasSize(1)))
                .andExpect(jsonPath("$.pictures.[0].picture").value(picture.getUrl()))
                .andDo(print());
    }
}