package com.todomypet.communityservice.service;

import com.todomypet.communityservice.domain.node.User;

public interface UserService {
    User findById(String userId);
}
