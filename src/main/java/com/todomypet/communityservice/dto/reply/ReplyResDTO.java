package com.todomypet.communityservice.dto.reply;

import com.todomypet.communityservice.dto.user.WriterResDTO;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReplyResDTO {
    private String id;
    private WriterResDTO writer;
    private String createdAt;
    private String content;
}
