package com.yu.jangtari.repository.post;

import com.yu.jangtari.domain.Post;


public interface CustomPostRepository {

//    public PageMakerVO<PostDTO.GetAll> getPostList(Long categoryId, PageVO pageVO, String type, String keyword);

    public Post getOne(Long postId);

//    public List<Hashtag> getHashtags(List<String> hashtags);
//
//    public List<CommentDTO.Get> getCommentsOfPost(Long postId);
}
