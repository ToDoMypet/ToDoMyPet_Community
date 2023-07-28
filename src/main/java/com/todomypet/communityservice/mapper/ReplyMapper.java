package com.todomypet.communityservice.mapper;

import com.todomypet.communityservice.domain.node.Reply;
import com.todomypet.communityservice.dto.reply.ReplyResDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReplyMapper {
    ReplyResDTO replyToReplyResDTO(Reply reply);

}
