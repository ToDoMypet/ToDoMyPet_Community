package com.todomypet.communityservice.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostResDTO {
    private PostInfoResDTO postInfo;
    private WriterResDTO writer;
}
