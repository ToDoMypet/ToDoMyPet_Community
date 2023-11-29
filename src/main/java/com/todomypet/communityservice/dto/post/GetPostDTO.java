package com.todomypet.communityservice.dto.post;

import com.todomypet.communityservice.dto.user.GetWriterDTO;
import com.todomypet.communityservice.dto.user.WriterResDTO;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetPostDTO {
    private GetPostInfoDTO postInfo;
    private GetWriterDTO writer;
}

