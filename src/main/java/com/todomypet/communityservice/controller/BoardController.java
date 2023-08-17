package com.todomypet.communityservice.controller;

import com.todomypet.communityservice.dto.*;
import com.todomypet.communityservice.dto.post.*;
import com.todomypet.communityservice.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("")
    public SuccessResDTO<WritePostResDTO> WritePost(@RequestHeader String userId,
                                                    @RequestPart(value="postInfo") WritePostReqDTO writePostReqDTO,
                                                    @RequestPart(value="imageUrls", required = false) List<MultipartFile> multipartFileList) {
        String responseId = boardService.post(userId, writePostReqDTO, multipartFileList);
        WritePostResDTO writePostResDTO = WritePostResDTO.builder().id(responseId).build();
        return new SuccessResDTO<WritePostResDTO>(writePostResDTO);
    }

    @GetMapping("")
    public SuccessResDTO<BoardListResDTO> getFeed(@RequestHeader String userId,
                                                  @RequestParam(required = false) String nextIndex,
                                                  @RequestParam(required = false, defaultValue = "20") int pageSize) {
        BoardListResDTO response = boardService.getFeed(userId, nextIndex, pageSize);
        return new SuccessResDTO<BoardListResDTO>(response);
    }

    @DeleteMapping("/{postId}")
    public SuccessResDTO<Void> deletePost(@RequestHeader String userId, @PathVariable String postId) {
        boardService.deletePost(userId, postId);
        return new SuccessResDTO<>(null);
    }

    @PutMapping("/{postId}")
    public SuccessResDTO<PostUpdateResDTO> updatePost(@RequestHeader String userId, @PathVariable String postId,
                                                      @RequestPart(value = "postInfo") PostUpdateReqDTO postUpdateReqDTO,
                                                      @RequestPart(value = "imageUrls", required = false) List<MultipartFile> multipartFileList) {
        boardService.updatePost(userId, postId, postUpdateReqDTO, multipartFileList);
        PostUpdateResDTO response = PostUpdateResDTO.builder().id(postId).build();
        return new SuccessResDTO<PostUpdateResDTO>(response);
    }

    @GetMapping("/my-post-list")
    public SuccessResDTO<BoardListResDTO> getMyPostList(@RequestHeader String userId,
                                                        @RequestParam(required = false) String nextIndex,
                                                        @RequestParam(required = false, defaultValue = "20") int pageSize) {
        BoardListResDTO response = boardService.getMyPostList(userId, nextIndex, pageSize);
        return new SuccessResDTO<BoardListResDTO>(response);
    }
}
