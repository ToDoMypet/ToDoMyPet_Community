package com.todomypet.communityservice.dto.reply;

import com.todomypet.communityservice.domain.node.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendReplyNotificationReqDTO {
    private String userId;
    private NotificationType type;
    private String senderProfilePicUrl;
    private String senderName;
    private String notificationDataId;
    private String notificationContent;
}
