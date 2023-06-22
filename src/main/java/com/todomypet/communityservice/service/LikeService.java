package com.todomypet.communityservice.service;

public interface LikeService {
    void likePost(String userId, String postId);
    void unlikePost(String userId, String postId);
}
