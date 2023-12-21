package com.todomypet.communityservice.dto.reply;

import lombok.Builder;

@Builder
public class SendReplyNotificationReqDTO {
    private String userId;
    private String type;
    private String senderProfilePicUrl;
    private String senderName;
    private String notificationDataId;
    private String notificationContent;
}
