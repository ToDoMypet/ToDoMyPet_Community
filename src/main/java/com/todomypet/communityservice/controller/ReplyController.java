package com.todomypet.communityservice.controller;

import com.todomypet.communityservice.dto.reply.*;
import com.todomypet.communityservice.dto.SuccessResDTO;
import com.todomypet.communityservice.service.ReplyService;
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
@Tag(name = "Reply Controller")
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "댓글 등록", description = "특정 글에 댓글을 등록합니다.")
    @PostMapping("/{postId}/reply")
    public SuccessResDTO<PostReplyResDTO> postReply(@Parameter(hidden = true) @RequestHeader String userId,
                                                    @PathVariable String postId,
                                                    @RequestBody PostReplyReqDTO postReplyReqDTO) {
        String responseId = replyService.postReply(userId, postId, postReplyReqDTO);
        PostReplyResDTO response = PostReplyResDTO.builder().id(responseId).build();
        return new SuccessResDTO<PostReplyResDTO>(response);
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/{postId}/reply/{replyId}")
    public SuccessResDTO<Void> deleteReply(@Parameter(hidden = true) @RequestHeader String userId,
                                           @PathVariable String postId,
                                           @PathVariable String replyId) {
        replyService.deleteReply(userId, postId, replyId);
        return new SuccessResDTO<Void>(null);
    }

    @Operation(summary = "댓글 목록 조회", description = "특정 글에 등록된 댓글 목록을 조회합니다.")
    @GetMapping("/{postId}/reply")
    public SuccessResDTO<ReplyListResDTO> getReplyList(@Parameter(hidden = true) @RequestHeader String userId,
                                                       @PathVariable String postId,
                                                       @RequestParam(required = false) String nextIndex,
                                                       @RequestParam(required = false, defaultValue = "20") int pageSize) {
        ReplyListResDTO response = replyService.getReplyList(userId, postId, nextIndex, pageSize);
        return new SuccessResDTO<ReplyListResDTO>(response);
    }

    @Operation(summary = "댓글 수정", description = "댓글 내용을 수정합니다.")
    @PutMapping("/{postId}/reply/{replyId}")
    public SuccessResDTO<ReplyUpdateResDTO> updateReply(@Parameter(hidden = true) @RequestHeader String userId,
                                                        @PathVariable String postId,
                                                        @PathVariable String replyId,
                                                        @RequestBody ReplyUpdateReqDTO replyUpdateReqDTO) {
        replyService.updateReply(userId, postId, replyId, replyUpdateReqDTO);
        ReplyUpdateResDTO response = ReplyUpdateResDTO.builder().postId(postId).build();
        return new SuccessResDTO<ReplyUpdateResDTO>(response);
    }
}
