package com.todomypet.communityservice.dto.reply;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class GetReplyInfoDTO {
    private String id;
    private String content;
    private LocalDateTime createdAt;
}
