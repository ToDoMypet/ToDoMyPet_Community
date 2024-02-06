package com.todomypet.communityservice.dto.post;

import com.todomypet.communityservice.dto.ect.PageDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class BoardListResDTO {
    private List<PostResDTO> postList;
    private PageDTO pagingInfo;
}
