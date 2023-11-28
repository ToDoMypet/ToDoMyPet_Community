package com.todomypet.communityservice.dto.post;

import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WritePostReqDTO {
    private String content;
    private List<String> imageUrls;
    private String petId;
    private String backgroundId;
}
