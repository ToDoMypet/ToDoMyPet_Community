package com.todomypet.communityservice.service;

import com.todomypet.communityservice.domain.node.Post;
import com.todomypet.communityservice.domain.node.Reply;
import com.todomypet.communityservice.domain.node.User;
import com.todomypet.communityservice.dto.reply.PostReplyReqDTO;
import com.todomypet.communityservice.exception.CustomException;
import com.todomypet.communityservice.exception.ErrorCode;
import com.todomypet.communityservice.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final ToRepository toRepository;
    private final WriteRepository writeRepository;

    @Override
    @Transactional
    public String postReply(String userId, String postId, PostReplyReqDTO postReplyReqDTO) {
        Reply reply = Reply.builder()
                .content(postReplyReqDTO.getContent())
                .createdAt(LocalDateTime.parse(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))))
                .deleted(false)
                .build();
        String responseId = replyRepository.save(reply).getId();

        toRepository.setToBetweenPostAndReply(postId, responseId);
        writeRepository.setWriteBetweenReplyAndUser(userId, responseId);
        postRepository.increaseReplyCountById(postId);

        return responseId;
    }

    @Override
    public void deleteReply(String userId, String postId, String replyId) {
        Post post = postRepository.findPostById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXIST));
        if (post.getDeleted()) {
            throw new CustomException(ErrorCode.DELETED_POST);
        }
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        Reply reply = replyRepository.findReplyById(replyId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPLY_NOT_EXISTS));
        if (reply.getDeleted()) {
            throw new CustomException(ErrorCode.DELETED_REPLY);
        }
        User writer = userRepository.findWriterByReplyId(replyId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        if (!writer.getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS_TO_POST);
        }
        replyRepository.deleteReplyById(replyId);
        postRepository.decreaseReplyCountById(postId);
    }
}
