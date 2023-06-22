package com.todomypet.communityservice.service;

import com.todomypet.communityservice.dto.PostReqDTO;

public interface BoardService {
    String post(String userId, PostReqDTO postReqDTO);
    void deletePost(String userId, String postId);
}
