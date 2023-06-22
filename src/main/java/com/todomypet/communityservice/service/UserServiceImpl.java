package com.todomypet.communityservice.service;

import com.todomypet.communityservice.domain.node.User;
import com.todomypet.communityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    UserRepository userRepository;

    @Override
    public User findById(String userId) {
        return userRepository.findById(userId).orElseThrow();
    }
}
