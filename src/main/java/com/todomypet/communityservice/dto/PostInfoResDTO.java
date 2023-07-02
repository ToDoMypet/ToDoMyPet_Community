package com.todomypet.communityservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PostInfoResDTO {
    private String content;
    private LocalDateTime createdAt;
    private List<String> imageUrl;
    private Integer likeCount;
    private Integer replyCount;
}
