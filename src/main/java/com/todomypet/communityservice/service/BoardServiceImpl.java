package com.todomypet.communityservice.service;

import com.todomypet.communityservice.domain.node.Post;
import com.todomypet.communityservice.domain.node.User;
import com.todomypet.communityservice.dto.*;
import com.todomypet.communityservice.exception.CustomException;
import com.todomypet.communityservice.exception.ErrorCode;
import com.todomypet.communityservice.repository.PostRepository;
import com.todomypet.communityservice.repository.UserRepository;
import com.todomypet.communityservice.repository.WriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final WriteRepository writeRepository;
    private final S3Uploader s3Uploader;

    @Override
    @Transactional
    public String post(String userId, WritePostReqDTO writePostReqDTO, List<MultipartFile> multipartFileList) {
        if (writePostReqDTO.getContent() == null) {
            throw new CustomException(ErrorCode.POST_CONTENT_NULL);
        }

        User user = userRepository.findUserById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        List<String> imgList = new ArrayList<>();
        if (multipartFileList != null) {
            for (MultipartFile multipartFile : multipartFileList) {
                imgList.add(s3Uploader.upload(multipartFile));
            }
        }

        Post post = Post.builder()
                .createdAt(LocalDateTime.parse(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))))
                .content(writePostReqDTO.getContent())
                .deleted(false)
                .replyCount(0)
                .likeCount(0)
                .imageUrl(imgList)
                .build();
        String responseId = postRepository.save(post).getId();
        writeRepository.setWriteRelationship(userId, responseId);

        return responseId;
    }

    @Override
    public void deletePost(String userId, String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXIST));
        if (post.getDeleted()) {
            throw new CustomException(ErrorCode.DELETED_POST);
        }
        User user = userRepository.findWriterByPostId(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        if (!user.getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS_TO_POST);
        }
        postRepository.deletePostById(postId);
    }

    @Override
    public void updatePost(String userId, String postId, PostUpdateReqDTO postUpdateReqDTO, List<MultipartFile> multipartFileList) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXIST));
        if (post.getDeleted()) {
            throw new CustomException(ErrorCode.DELETED_POST);
        }
        User user = userRepository.findWriterByPostId(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        if (!user.getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS_TO_POST);
        }

        List<String> imgList = new ArrayList<>();
        if (multipartFileList != null) {
            for (MultipartFile multipartFile : multipartFileList) {
                imgList.add(s3Uploader.upload(multipartFile));
            }
        }

        postRepository.updatePost(postId, postUpdateReqDTO.getContent(), imgList.toString());
    }

    @Override
    public BoardListResDTO getMyPostList(String userId) {
        List<GetPostDTO> getPostDTOList = postRepository.getPostListByUserId(userId);
        BoardListResDTO boardListResDTO = BoardListResDTO.builder().postList(getPostDTOList).build();
        return boardListResDTO;
    }
}
