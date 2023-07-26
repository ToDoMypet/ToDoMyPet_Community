package com.todomypet.communityservice.controller;

import com.todomypet.communityservice.dto.SuccessResDTO;
import com.todomypet.communityservice.dto.like.LikeUserListResDTO;
import com.todomypet.communityservice.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}/like")
    public SuccessResDTO<Void> likePost(@RequestHeader String userId, @PathVariable String postId) {
        likeService.likePost(userId, postId);
        return new SuccessResDTO<>(null);
    }

    @DeleteMapping("/{postId}/unlike")
    public SuccessResDTO<Void> unlikePost(@RequestHeader String userId, @PathVariable String postId) {
        likeService.unlikePost(userId, postId);
        return new SuccessResDTO<>(null);
    }

    @GetMapping("/{postId}/like/list")
    public SuccessResDTO<LikeUserListResDTO> getLikeUserList(@RequestHeader String userId,
                                                             @PathVariable String postId) {
        LikeUserListResDTO response = likeService.getLikeUserList(userId, postId);
        return new SuccessResDTO<LikeUserListResDTO>(response);
    }
}
