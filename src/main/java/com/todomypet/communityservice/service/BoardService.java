package com.todomypet.communityservice.service;

import com.todomypet.communityservice.dto.PostReqDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    String post(String userId, PostReqDTO postReqDTO, List<MultipartFile> multipartFileList);
    void deletePost(String userId, String postId);
}
