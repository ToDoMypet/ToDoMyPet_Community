package com.todomypet.communityservice.controller;

import com.todomypet.communityservice.dto.*;
import com.todomypet.communityservice.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board")
    public SuccessResDTO<PostResDTO> post(@RequestHeader String userId,
                                          @RequestPart(value="postInfo") PostReqDTO postReqDTO,
                                          @RequestPart(value="imageUrls", required = false) List<MultipartFile> multipartFileList) {
        String responseId = boardService.post(userId, postReqDTO, multipartFileList);
        PostResDTO postResDTO = PostResDTO.builder().id(responseId).build();
        return new SuccessResDTO<PostResDTO>(postResDTO);
    }

    @DeleteMapping("/board/{postId}")
    public SuccessResDTO<Void> deletePost(@RequestHeader String userId, @PathVariable String postId) {
        boardService.deletePost(userId, postId);
        return new SuccessResDTO<>(null);
    }

    @PutMapping("/board/{postId}")
    public SuccessResDTO<PostUpdateResDTO> updatePost(@RequestHeader String userId, @PathVariable String postId,
                                                      @RequestPart(value = "postInfo") PostUpdateReqDTO postUpdateReqDTO,
                                                      @RequestPart(value = "imageUrls", required = false) List<MultipartFile> multipartFileList) {
        boardService.updatePost(userId, postId, postUpdateReqDTO, multipartFileList);
        PostUpdateResDTO response = PostUpdateResDTO.builder().id(postId).build();
        return new SuccessResDTO<PostUpdateResDTO>(response);
    }
}
