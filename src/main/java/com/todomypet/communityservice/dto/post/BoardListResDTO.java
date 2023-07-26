package com.todomypet.communityservice.dto.post;

import com.todomypet.communityservice.dto.PageDTO;
import com.todomypet.communityservice.dto.post.GetPostDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class BoardListResDTO {
    private List<GetPostDTO> postList;
    private PageDTO pagingInfo;
}
