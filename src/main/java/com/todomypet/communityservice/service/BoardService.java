package com.todomypet.communityservice.service;

import com.todomypet.communityservice.dto.post.BoardListResDTO;
import com.todomypet.communityservice.dto.post.WritePostReqDTO;
import com.todomypet.communityservice.dto.post.PostUpdateReqDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    String post(String userId, WritePostReqDTO writePostReqDTO, List<MultipartFile> multipartFileList);
    void deletePost(String userId, String postId);

    void updatePost(String userId, String postId, PostUpdateReqDTO postUpdateReqDTO, List<MultipartFile> multipartFileList);

    BoardListResDTO getMyPostList(String userId);
}
