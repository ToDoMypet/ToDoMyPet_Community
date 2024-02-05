package com.todomypet.communityservice.service;

import com.todomypet.communityservice.dto.post.BoardListResDTO;
import com.todomypet.communityservice.dto.post.PostResDTO;
import com.todomypet.communityservice.dto.post.WritePostReqDTO;
import com.todomypet.communityservice.dto.post.PostUpdateReqDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    String post(String userId, WritePostReqDTO writePostReqDTO);
    void deletePost(String userId, String postId);

    void updatePost(String userId, String postId, PostUpdateReqDTO postUpdateReqDTO);

    BoardListResDTO getMyPostList(String userId, String nextIndex, int pageSize);

    BoardListResDTO getFeed(String userId, String nextIndex, int pageSize);

    PostResDTO getPostDetailById(String userId, String postId);

    void reportPost(String userId, String postId);
}
