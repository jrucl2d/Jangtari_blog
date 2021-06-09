package com.yu.jangtari.controller;

import com.yu.jangtari.domain.DTO.PostDTO;
import com.yu.jangtari.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.stream.IntStream;


@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO.GetOne getPost(@PathVariable(value = "id") Long postId){
        return PostDTO.GetOne.of(postService.findOne(postId));
    }
//    @GetMapping("/category/{id}/posts")
//    @ResponseStatus(HttpStatus.OK)


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
}
