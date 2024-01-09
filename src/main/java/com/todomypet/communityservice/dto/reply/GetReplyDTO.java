package com.todomypet.communityservice.dto.reply;

import com.todomypet.communityservice.dto.user.WriterResDTO;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetReplyDTO {
    private GetReplyInfoDTO replyInfo;
    private WriterResDTO writer;
}
