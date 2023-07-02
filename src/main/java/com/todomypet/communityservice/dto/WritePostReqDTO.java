package com.todomypet.communityservice.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WritePostReqDTO {
    private String content;
}
