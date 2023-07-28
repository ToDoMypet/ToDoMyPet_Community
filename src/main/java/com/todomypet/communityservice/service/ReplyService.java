package com.todomypet.communityservice.service;

import com.todomypet.communityservice.dto.reply.PostReplyReqDTO;
import com.todomypet.communityservice.dto.reply.ReplyListResDTO;
import com.todomypet.communityservice.dto.reply.ReplyResDTO;

public interface ReplyService {
    String postReply(String userId, String postId, PostReplyReqDTO postReplyReqDTO);
    void deleteReply(String userId, String postId, String replyId);
    ReplyListResDTO getReplyList(String postId, String nextIndex, int pageSize);
}
