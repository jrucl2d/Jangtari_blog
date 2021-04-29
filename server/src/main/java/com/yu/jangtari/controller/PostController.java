//package com.yu.jangtari.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.yu.jangtari.common.ErrorResponse;
//import com.yu.jangtari.common.CustomException;
//import com.yu.jangtari.common.CustomResponse;
//import com.yu.jangtari.domain.Category;
//import com.yu.jangtari.domain.Post;
//import com.yu.jangtari.repository.post.PostRepository;
//import com.yu.jangtari.vo.PageMakerVO;
//import com.yu.jangtari.vo.PageVO;
//import com.yu.jangtari.domain.DTO.PostDTO;
//import com.yu.jangtari.service.PostService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.util.List;
//import java.util.stream.IntStream;
//
//
//@Controller
//public class PostController {
//
//    @Autowired
//    private PostService postService;
//
//    @Autowired
//    private PostRepository postRepository;
//
//
//    @GetMapping("almighty/hashtag/clean")
//    public @ResponseBody String cleanTrashHashtags(){
//        postService.deleteTrashHashtags();
//        return "haha";
//    }
//
//    @GetMapping("/bulk")
//    public ResponseEntity<CustomResponse> theBulk(){
//        IntStream.range(0, 300).forEach(i -> {
//            Post post = new Post();
//            post.setTitle("벌크 " + i);
//            post.setPost("내용 " + i);
//            Category category = new Category();
//            category.setId(1L);
//            post.setCategory(category);
//            postRepository.save(post);
//        });
//
//        return new ResponseEntity<>(new CustomResponse<>
//                (null, "good"),
//                HttpStatus.OK);
//
//    }
//
//    @GetMapping("/category/{id}/posts")
//    public ResponseEntity<CustomResponse> getPostList(@PathVariable(value = "id") Long categoryId, PageVO pageVO){
//        try{
//            PageMakerVO<PostDTO.GetAll> results =  postService.getPostList(categoryId ,pageVO, pageVO.getType(), pageVO.getKeyword());
//            return new ResponseEntity<>(new CustomResponse<>
//                    (null, results),
//                    HttpStatus.OK);
//        } catch(CustomException e){
//            return new ResponseEntity<>(new CustomResponse<>
//                    (new ErrorResponse(e), null),
//                    HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @GetMapping("/post/{id}")
//    public ResponseEntity<CustomResponse> getPost(@PathVariable(value = "id") Long postId){
//        PostDTO.GetOne result = postService.getPost(postId);
//        return new ResponseEntity<>(new CustomResponse<>
//                (null, result),
//                HttpStatus.OK);
//    }
//
//    @PostMapping("/admin/post")
//    public ResponseEntity<CustomResponse> addPost(@RequestPart("post") String postString,
//                                                  @RequestPart("images") List<MultipartFile> postImages) throws GeneralSecurityException, IOException {
//        try{
//            PostDTO.Add thePost = new ObjectMapper().readValue(postString, PostDTO.Add.class);
//            postService.addPost(thePost, postImages);
//            return new ResponseEntity<>(CustomResponse.OK(),
//                    HttpStatus.CREATED);
//        } catch (CustomException e){
//            return new ResponseEntity<>(new CustomResponse<>
//                    (new ErrorResponse(e), null),
//                    HttpStatus.BAD_REQUEST);
//        }
//    }
//    @PostMapping("/admin/post/nimg")
//    public ResponseEntity<CustomResponse> addPost2(@RequestPart("post") String postString) throws GeneralSecurityException, IOException {
//        try{
//            PostDTO.Add thePost = new ObjectMapper().readValue(postString, PostDTO.Add.class);
//            postService.addPost(thePost, null);
//            return new ResponseEntity<>(CustomResponse.OK(),
//                    HttpStatus.CREATED);
//        } catch (CustomException e){
//            return new ResponseEntity<>(new CustomResponse<>
//                    (new ErrorResponse(e), null),
//                    HttpStatus.BAD_REQUEST);
//        }
//    }
//    @PutMapping("/admin/post")
//    public ResponseEntity<CustomResponse> updatePost(@RequestPart("post") String postString,
//                                                  @RequestPart("images") List<MultipartFile> postImages) throws GeneralSecurityException, IOException {
//        try{
//            PostDTO.Update thePost = new ObjectMapper().readValue(postString, PostDTO.Update.class);
//            postService.updatePost(thePost, postImages);
//            return new ResponseEntity<>(CustomResponse.OK(),
//                    HttpStatus.CREATED);
//        } catch (CustomException e){
//            return new ResponseEntity<>(new CustomResponse<>
//                    (new ErrorResponse(e), null),
//                    HttpStatus.BAD_REQUEST);
//        }
//    }
//    @PutMapping("/admin/post/nimg")
//    public ResponseEntity<CustomResponse> updatePost2(@RequestPart("post") String postString) throws GeneralSecurityException, IOException {
//        try{
//            PostDTO.Update thePost = new ObjectMapper().readValue(postString, PostDTO.Update.class);
//            postService.updatePost(thePost, null);
//            return new ResponseEntity<>(CustomResponse.OK(),
//                    HttpStatus.CREATED);
//        } catch (CustomException e){
//            return new ResponseEntity<>(new CustomResponse<>
//                    (new ErrorResponse(e), null),
//                    HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @DeleteMapping("/admin/post/{id}")
//    public ResponseEntity<CustomResponse> deletePost(@PathVariable(value = "id")Long postId){
//        try{
//            postService.deletePost(postId);
//            return new ResponseEntity<>(CustomResponse.OK(),
//                    HttpStatus.CREATED);
//        } catch (CustomException e){
//            return new ResponseEntity<>(new CustomResponse<>
//                    (new ErrorResponse(e), null),
//                    HttpStatus.BAD_REQUEST);
//        }
//    }
//}
