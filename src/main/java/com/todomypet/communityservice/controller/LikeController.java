package com.todomypet.communityservice.controller;

import com.todomypet.communityservice.dto.SuccessResDTO;
import com.todomypet.communityservice.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/board/{postId}/like")
    public SuccessResDTO<Void> likePost(@RequestHeader String userId, @PathVariable String postId) {
        likeService.likePost(userId, postId);
        return new SuccessResDTO<>(null);
    }

    @DeleteMapping("/board/{postId}/unlike")
    public SuccessResDTO<Void> unlikePost(@RequestHeader String userId, @PathVariable String postId) {
        likeService.unlikePost(userId, postId);
        return new SuccessResDTO<>(null);
    }
}
