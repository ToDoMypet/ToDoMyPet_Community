package com.todomypet.communityservice.dto.ect;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PageDTO {
    private String nextIndex;
    private boolean hasNextPage;
}
