package com.todomypet.communityservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class BoardListResDTO {
    private List<GetPostDTO> postList;
    private PageDTO pagingInfo;
}
