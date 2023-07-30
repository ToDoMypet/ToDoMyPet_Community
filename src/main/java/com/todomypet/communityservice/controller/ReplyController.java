package com.todomypet.communityservice.controller;

import com.todomypet.communityservice.dto.reply.*;
import com.todomypet.communityservice.dto.SuccessResDTO;
import com.todomypet.communityservice.service.ReplyService;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{postId}/reply")
    public SuccessResDTO<PostReplyResDTO> postReply(@RequestHeader String userId,
                                                    @PathVariable String postId,
                                                    @RequestBody PostReplyReqDTO postReplyReqDTO) {
        String responseId = replyService.postReply(userId, postId, postReplyReqDTO);
        PostReplyResDTO response = PostReplyResDTO.builder().id(responseId).build();
        return new SuccessResDTO<PostReplyResDTO>(response);
    }

    @DeleteMapping("/{postId}/reply/{replyId}")
    public SuccessResDTO<Void> deleteReply(@RequestHeader String userId,
                                           @PathVariable String postId,
                                           @PathVariable String replyId) {
        replyService.deleteReply(userId, postId, replyId);
        return new SuccessResDTO<Void>(null);
    }

    @GetMapping("/{postId}/reply")
    public SuccessResDTO<ReplyListResDTO> getReplyList(@RequestHeader String userId,
                                                       @PathVariable String postId,
                                                       @RequestParam(required = false) String nextIndex,
                                                       @RequestParam(required = false, defaultValue = "20") int pageSize) {
        ReplyListResDTO response = replyService.getReplyList(postId, nextIndex, pageSize);
        return new SuccessResDTO<ReplyListResDTO>(response);
    }

    @PutMapping("/{postId}/reply/{replyId}")
    public SuccessResDTO<ReplyUpdateResDTO> updateReply(@RequestHeader String userId,
                                                        @PathVariable String postId,
                                                        @PathVariable String replyId,
                                                        @RequestBody ReplyUpdateReqDTO replyUpdateReqDTO) {
        replyService.updateReply(userId, postId, replyId, replyUpdateReqDTO);
        ReplyUpdateResDTO response = ReplyUpdateResDTO.builder().postId(postId).build();
        return new SuccessResDTO<ReplyUpdateResDTO>(response);
    }
}
