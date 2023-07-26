package com.todomypet.communityservice.service;

import com.todomypet.communityservice.dto.like.LikeUserListResDTO;

public interface LikeService {
    void likePost(String userId, String postId);
    void unlikePost(String userId, String postId);

    LikeUserListResDTO getLikeUserList(String userId, String postId);
}
