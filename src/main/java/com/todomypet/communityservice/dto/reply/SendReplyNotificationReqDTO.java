package com.todomypet.communityservice.dto.reply;

import com.todomypet.communityservice.domain.node.NotificationType;
import lombok.Builder;

@Builder
public class SendReplyNotificationReqDTO {
    private String userId;
    private NotificationType type;
    private String senderProfilePicUrl;
    private String senderName;
    private String notificationDataId;
    private String notificationContent;
}
