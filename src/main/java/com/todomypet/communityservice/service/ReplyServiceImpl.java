package com.todomypet.communityservice.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.todomypet.communityservice.domain.node.NotificationType;
import com.todomypet.communityservice.domain.node.Post;
import com.todomypet.communityservice.domain.node.Reply;
import com.todomypet.communityservice.domain.node.User;
import com.todomypet.communityservice.dto.ect.PageDTO;
import com.todomypet.communityservice.dto.ect.ReportDTO;
import com.todomypet.communityservice.dto.ect.ReportType;
import com.todomypet.communityservice.dto.reply.*;
import com.todomypet.communityservice.dto.user.WriterResDTO;
import com.todomypet.communityservice.exception.CustomException;
import com.todomypet.communityservice.exception.ErrorCode;
import com.todomypet.communityservice.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ReplyServiceImpl implements ReplyService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final ToRepository toRepository;
    private final WriteRepository writeRepository;
    private final NotificationServiceClient notificationServiceClient;
    private final MailService mailService;

    @Override
    @Transactional
    public String postReply(String userId, String postId, PostReplyReqDTO postReplyReqDTO) {
        User sender = userRepository.findUserById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        Reply reply = Reply.builder()
                .id(UlidCreator.getUlid().toString())
                .content(postReplyReqDTO.getContent())
                .createdAt(LocalDateTime.parse(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))))
                .deleted(false)
                .build();
        String responseId = replyRepository.save(reply).getId();


        toRepository.setToBetweenPostAndReply(postId, responseId);
        writeRepository.setWriteBetweenReplyAndUser(userId, responseId);
        postRepository.increaseReplyCountById(postId);

        User writer = userRepository.findWriterByPostId(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        if (!writer.getId().equals(userId)) {
            notificationServiceClient.sendReplyNotification(SendReplyNotificationReqDTO.builder()
                    .userId(writer.getId()).type(NotificationType.REPLY)
                    .senderProfilePicUrl(sender.getProfilePicUrl()).senderName(sender.getNickname())
                    .notificationDataId(postId).notificationContent(reply.getContent()).build());
        }

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
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        replyRepository.deleteReplyById(replyId);
        postRepository.decreaseReplyCountById(postId);
    }

    @Override
    public ReplyListResDTO getReplyList(String userId, String postId, String nextIndex, int pageSize) {
        if (nextIndex == null) {
            nextIndex = UlidCreator.getUlid().toString();
        }
        List<Reply> replyList = replyRepository.getReplyListByPostId(postId, nextIndex, pageSize);
        ArrayList<ReplyResDTO> replyResDtoList = new ArrayList<>();
        for (Reply r : replyList) {
            User writer = r.getWriter();
            replyResDtoList.add(ReplyResDTO.builder().replyInfo(GetReplyInfoDTO.builder().id(r.getId())
                            .createdAt(r.getCreatedAt()).content(r.getContent()).build())
                    .writer(WriterResDTO.builder().id(writer.getId()).nickname(writer.getNickname())
                            .profilePicUrl(writer.getProfilePicUrl())
                            .deleted(writer.getDeleted())
                            .isMyPost(writeRepository.existsWriteBetweenUserAndReply(userId, r.getId())).build()).build());
        }
        PageDTO pageInfo;
        if (replyResDtoList.size() > pageSize) {
            pageInfo = PageDTO.builder().nextIndex(replyResDtoList.get(pageSize).getReplyInfo().getId()).hasNextPage(true).build();
            replyResDtoList.remove(pageSize);
        } else {
            pageInfo = PageDTO.builder().nextIndex(null).hasNextPage(false).build();
        }
        return ReplyListResDTO.builder().replyList(replyResDtoList).pageInfo(pageInfo).build();
    }

    @Override
    public void updateReply(String userId, String postId, String replyId, ReplyUpdateReqDTO updateInfo) {
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
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        String content = updateInfo.getContent();
        if (content == null || content.isEmpty()) {
            throw new CustomException(ErrorCode.REPLY_CONTENT_NULL);
        }
        replyRepository.update(replyId, updateInfo.getContent());
    }

    @Override
    public void reportReply(String userId, String replyId) {
        Reply reply = replyRepository.findReplyById(replyId).orElseThrow();
        User writer = userRepository.findWriterByReplyId(replyId).orElseThrow();
        ReportDTO reportInfo = ReportDTO.builder().reportType(ReportType.REPLY).reportedId(replyId)
                .reportedContent(reply.getContent()).reporterId(userId)
                .writerId(writer.getId())
                .writerNickname(writer.getNickname()).build();
        try {
            mailService.sendReportMail(reportInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.MAIL_SEND_FAIL);
        }
    }
}
