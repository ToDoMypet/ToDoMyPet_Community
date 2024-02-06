package com.todomypet.communityservice.dto.post;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdminGetAllPostDTO {
    List<PostResDTO> postList;
}
