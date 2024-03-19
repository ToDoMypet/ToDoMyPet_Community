package com.todomypet.communityservice.service;

import com.todomypet.communityservice.dto.reply.*;

public interface ReplyService {
    String postReply(String userId, String postId, PostReplyReqDTO postReplyReqDTO);
    void deleteReply(String userId, String postId, String replyId);
    ReplyListResDTO getReplyList(String userId, String postId, String nextIndex, int pageSize);

    void updateReply(String userId, String postId, String replyId, ReplyUpdateReqDTO updateInfo);

    void reportReply(String userId, String replyId);
}
