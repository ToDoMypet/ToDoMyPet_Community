package com.todomypet.communityservice.service;

import com.todomypet.communityservice.dto.post.FeignClientResDTO;
import com.todomypet.communityservice.dto.reply.SendReplyNotificationReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="notification-service", url = "${feign.notification.url}")
public interface NotificationServiceClient {
    @PostMapping(value = "/send/by-action", consumes = "application/json")
    FeignClientResDTO<Void> sendReplyNotification(@RequestBody SendReplyNotificationReqDTO sendReplyNotificationReqDTO);
}
