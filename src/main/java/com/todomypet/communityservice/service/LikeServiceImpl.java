package com.todomypet.communityservice.service;

import com.todomypet.communityservice.domain.node.Post;
import com.todomypet.communityservice.domain.node.User;
import com.todomypet.communityservice.exception.CustomException;
import com.todomypet.communityservice.exception.ErrorCode;
import com.todomypet.communityservice.repository.LikeRepository;
import com.todomypet.communityservice.repository.PostRepository;
import com.todomypet.communityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public void likePost(String userId, String postId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXIST));
        if (post.getDeleted()) {
            throw new CustomException(ErrorCode.DELETED_POST);
        }
        if (likeRepository.existsLikeByUserAndPost(userId, postId)) {
            throw new CustomException(ErrorCode.ALREADY_EXISTS_RELATIONSHIP);
        }
        likeRepository.like(userId, postId);
        postRepository.increaseLikeCountById(postId);
    }

    @Override
    @Transactional
    public void unlikePost(String userId, String postId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXIST));
        if (post.getDeleted()) {
            throw new CustomException(ErrorCode.DELETED_POST);
        }
        if (!likeRepository.existsLikeByUserAndPost(userId, postId)) {
            throw new CustomException(ErrorCode.NOT_EXISTS_RELATIONSHIP);
        }
        likeRepository.unlike(userId, postId);
        postRepository.decreaseLikeCountById(postId);
    }
}
