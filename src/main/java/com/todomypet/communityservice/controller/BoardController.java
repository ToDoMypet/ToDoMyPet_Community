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
    public SuccessResDTO<WritePostResDTO> WritePost(@RequestHeader String userId,
                                               @RequestPart(value="postInfo") WritePostReqDTO writePostReqDTO,
                                               @RequestPart(value="imageUrls", required = false) List<MultipartFile> multipartFileList) {
        String responseId = boardService.post(userId, writePostReqDTO, multipartFileList);
        WritePostResDTO writePostResDTO = WritePostResDTO.builder().id(responseId).build();
        return new SuccessResDTO<WritePostResDTO>(writePostResDTO);
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

    @GetMapping("/board/my")
    public SuccessResDTO<BoardListResDTO> getMyPostList(@RequestHeader String userId) {
        BoardListResDTO response = boardService.getMyPostList(userId);
        return new SuccessResDTO<BoardListResDTO>(response);
    }
}
