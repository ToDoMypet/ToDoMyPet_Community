package com.todomypet.communityservice.dto.post;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WritePostReqDTO {
    private String content;
}
