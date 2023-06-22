package com.todomypet.communityservice.service;

import com.todomypet.communityservice.dto.PostReplyReqDTO;

public interface ReplyService {
    String postReply(String userId, String postId, PostReplyReqDTO postReplyReqDTO);
    void deleteReply(String userId, String postId, String replyId);
}
