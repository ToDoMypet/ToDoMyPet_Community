package com.todomypet.communityservice.controller;

import com.todomypet.communityservice.dto.PostReqDTO;
import com.todomypet.communityservice.dto.PostResDTO;
import com.todomypet.communityservice.dto.SuccessResDTO;
import com.todomypet.communityservice.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board")
    public SuccessResDTO<PostResDTO> post(@RequestHeader String userId,
                                          @RequestPart(value="postInfo") PostReqDTO postReqDTO) {
        String responseId = boardService.post(userId, postReqDTO);
        PostResDTO postResDTO = PostResDTO.builder().id(responseId).build();
        return new SuccessResDTO<PostResDTO>(postResDTO);
    }

    @DeleteMapping("/board/{postId}")
    public SuccessResDTO<Void> deletePost(@RequestHeader String userId, @PathVariable String postId) {
        boardService.deletePost(userId, postId);
        return new SuccessResDTO<>(null);
    }
}
