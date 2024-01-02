package com.todomypet.communityservice.dto.post;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PostInfoResDTO {
    private String id;
    private String content;
    private LocalDateTime createdAt;
    private List<String> imageUrl;
    private Integer likeCount;
    private Integer replyCount;
    private String petId;
    private String petName;
    private String petGrade;
    private String petImageUrl;
    private String backgroundId;
    private String backgroundImageUrl;
    private boolean isLiked;
}
