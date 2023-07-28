package com.todomypet.communityservice.dto.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WriterResDTO {
    private String profilePicUrl;
    private String nickname;
}
