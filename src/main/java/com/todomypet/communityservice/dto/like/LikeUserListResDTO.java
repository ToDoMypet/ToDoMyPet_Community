package com.todomypet.communityservice.dto.like;

import com.todomypet.communityservice.dto.user.UserProfileResDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class LikeUserListResDTO {
    List<UserProfileResDTO> likeList;
}
