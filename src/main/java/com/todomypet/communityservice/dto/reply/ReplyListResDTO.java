package com.todomypet.communityservice.dto.reply;

import com.todomypet.communityservice.dto.PageDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReplyListResDTO {
    List<ReplyResDTO> replyList;
    PageDTO pageInfo;
}
