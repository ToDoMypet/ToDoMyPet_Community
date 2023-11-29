package com.todomypet.communityservice.dto.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetWriterDTO {
    private String id;
    private String profilePicUrl;
    private String nickname;
}
