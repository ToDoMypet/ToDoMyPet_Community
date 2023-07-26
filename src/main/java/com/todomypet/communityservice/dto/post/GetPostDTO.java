package com.todomypet.communityservice.dto.post;

import com.todomypet.communityservice.dto.WriterResDTO;
import com.todomypet.communityservice.dto.post.PostInfoResDTO;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetPostDTO {
    private PostInfoResDTO postInfo;
    private WriterResDTO writer;
}

