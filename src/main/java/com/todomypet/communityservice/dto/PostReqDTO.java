package com.todomypet.communityservice.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostReqDTO {
    private String content;
}
