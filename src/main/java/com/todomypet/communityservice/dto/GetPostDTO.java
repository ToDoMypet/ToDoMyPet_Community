package com.todomypet.communityservice.dto;

import com.todomypet.communityservice.domain.node.Post;
import com.todomypet.communityservice.domain.node.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetPostDTO {
    private PostInfoResDTO postInfo;
    private WriterResDTO writer;
}

