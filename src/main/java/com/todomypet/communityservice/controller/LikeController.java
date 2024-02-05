package com.todomypet.communityservice.controller;

import com.todomypet.communityservice.dto.ect.SuccessResDTO;
import com.todomypet.communityservice.dto.like.LikeUserListResDTO;
import com.todomypet.communityservice.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Like Controller")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요", description = "특정 글에 좋아요 표시를 합니다.")
    @PostMapping("/{postId}/like")
    public SuccessResDTO<Void> likePost(@Parameter(hidden = true) @RequestHeader String userId, @PathVariable String postId) {
        likeService.likePost(userId, postId);
        return new SuccessResDTO<>(null);
    }

    @Operation(summary = "좋아요 취소", description = "특정 글에 표시했던 좋아요를 취소합니다.")
    @DeleteMapping("/{postId}/unlike")
    public SuccessResDTO<Void> unlikePost(@Parameter(hidden = true) @RequestHeader String userId, @PathVariable String postId) {
        likeService.unlikePost(userId, postId);
        return new SuccessResDTO<>(null);
    }

    @Operation(summary = "좋아요 목록 조회", description = "특정 글에 좋아요를 누른 사용자 목록을 조회합니다.")
    @GetMapping("/{postId}/like/list")
    public SuccessResDTO<LikeUserListResDTO> getLikeUserList(@Parameter(hidden = true) @RequestHeader String userId,
                                                             @PathVariable String postId) {
        LikeUserListResDTO response = likeService.getLikeUserList(userId, postId);
        return new SuccessResDTO<LikeUserListResDTO>(response);
    }
}
