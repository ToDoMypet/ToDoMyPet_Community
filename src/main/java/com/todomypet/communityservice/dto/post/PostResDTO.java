package com.todomypet.communityservice.dto.post;

import com.todomypet.communityservice.dto.user.WriterResDTO;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostResDTO {
    private PostInfoResDTO postInfo;
    private WriterResDTO writer;
}
