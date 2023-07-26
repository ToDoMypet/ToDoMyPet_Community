package com.todomypet.communityservice.mapper;

import com.todomypet.communityservice.domain.node.User;
import com.todomypet.communityservice.dto.user.UserProfileResDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserProfileResDTO userToUserProfileResDTO(User user);
}
