package com.todomypet.communityservice.dto.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserProfileResDTO {
    private String nickname;
    private String bio;
    private String profilePicUrl;
}
