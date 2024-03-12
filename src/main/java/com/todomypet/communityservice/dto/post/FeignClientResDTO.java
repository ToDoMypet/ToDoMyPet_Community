package com.todomypet.communityservice.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FeignClientResDTO<T> {
    private String code;
    private String message;
    private T data;
}
