package com.todomypet.communityservice.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.todomypet.communityservice.domain.node.Post;
import com.todomypet.communityservice.domain.node.Reply;
import com.todomypet.communityservice.domain.node.User;
import com.todomypet.communityservice.dto.PageDTO;
import com.todomypet.communityservice.dto.pet.PetDetailResDTO;
import com.todomypet.communityservice.dto.post.*;
import com.todomypet.communityservice.dto.reply.ReplyListResDTO;
import com.todomypet.communityservice.dto.reply.ReplyResDTO;
import com.todomypet.communityservice.dto.user.WriterResDTO;
import com.todomypet.communityservice.exception.CustomException;
import com.todomypet.communityservice.exception.ErrorCode;
import com.todomypet.communityservice.repository.LikeRepository;
import com.todomypet.communityservice.repository.PostRepository;
import com.todomypet.communityservice.repository.UserRepository;
import com.todomypet.communityservice.repository.WriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final WriteRepository writeRepository;
    private final LikeRepository likeRepository;
    private final S3Uploader s3Uploader;
    private final PetServiceClient petServiceClient;

    // todo: 서버간 통신에 대한 예외 처리 메소드 만들기

    private PostResDTO getPostDTOToPostResDTO(String userId, GetPostDTO getPostDTO) {
        PetDetailResDTO petDetailResDTO =
                petServiceClient.getPetDetailInfo(getPostDTO.getWriter().getId(),
                        getPostDTO.getPostInfo().getPetId()).getData();
        GetPostInfoDTO postInfo = getPostDTO.getPostInfo();
        String backgroundImageUrl = petServiceClient.getBackgroundUrlById(postInfo.getBackgroundId()).getData();
        PostInfoResDTO postInfoResDTO = PostInfoResDTO.builder().id(postInfo.getId())
                .content(postInfo.getContent())
                .createdAt(postInfo.getCreatedAt())
                .imageUrl(postInfo.getImageUrl())
                .likeCount(postInfo.getLikeCount())
                .replyCount(postInfo.getReplyCount())
                .petName(petDetailResDTO.getName())
                .petGrade(petDetailResDTO.getGrade())
                .petImageUrl(petDetailResDTO.getPortraitUrl())
                .backgroundImageUrl(backgroundImageUrl)
                .isLiked(likeRepository.existsLikeByUserAndPost(userId, postInfo.getId())).build();
        WriterResDTO writerResDTO = WriterResDTO.builder().nickname(getPostDTO.getWriter().getNickname())
                .profilePicUrl(getPostDTO.getWriter().getProfilePicUrl())
                .isMyPost(writeRepository.existsWriteBetweenUserAndPost(userId, postInfo.getId())).build();

        return PostResDTO.builder().postInfo(postInfoResDTO).writer(writerResDTO).build();
    }

    private PageDTO createPageDTO(int pageSize, List<GetPostDTO> postList) {
        PageDTO pageInfo;
        if (postList.size() > pageSize) {
            pageInfo = PageDTO.builder().nextIndex(postList.get(pageSize).getPostInfo().getId())
                    .hasNextPage(true).build();
            postList.remove(pageSize);
        } else {
            pageInfo = PageDTO.builder().nextIndex(null).hasNextPage(false).build();
        }
        return pageInfo;
    }

    @Override
    @Transactional
    public String post(String userId, WritePostReqDTO writePostReqDTO) {
        if (writePostReqDTO.getContent() == null) {
            throw new CustomException(ErrorCode.POST_CONTENT_NULL);
        }

        User user = userRepository.findUserById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        List<String> imgList = new ArrayList<>();
        if (!writePostReqDTO.getImageUrls().isEmpty()) {
            for (String fileString : writePostReqDTO.getImageUrls()) {
                imgList.add(s3Uploader.upload(fileString));
            }
        }

        Post post = Post.builder()
                .id(UlidCreator.getUlid().toString())
                .createdAt(LocalDateTime.parse(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))))
                .content(writePostReqDTO.getContent())
                .deleted(false)
                .replyCount(0)
                .likeCount(0)
                .imageUrl(imgList)
                .petId(writePostReqDTO.getPetId())
                .backgroundId(writePostReqDTO.getBackgroundId())
                .build();
        String responseId = postRepository.save(post).getId();
        writeRepository.setWriteRelationship(userId, responseId, UlidCreator.getUlid().toString());

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
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        postRepository.deletePostById(postId);
    }

    @Override
    public void updatePost(String userId, String postId, PostUpdateReqDTO postUpdateReqDTO) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXIST));
        if (post.getDeleted()) {
            throw new CustomException(ErrorCode.DELETED_POST);
        }
        User user = userRepository.findWriterByPostId(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        if (!user.getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        List<String> imgList = new ArrayList<>();
        if (!postUpdateReqDTO.getImageUrls().isEmpty()) {
            for (String fileString : postUpdateReqDTO.getImageUrls()) {
                imgList.add(s3Uploader.upload(fileString));
            }
        }

        postRepository.updatePost(postId, postUpdateReqDTO.getContent(), imgList.toString());
    }

    @Override
    public BoardListResDTO getMyPostList(String userId, String nextIndex, int pageSize) {
        if (nextIndex == null) {
            nextIndex = UlidCreator.getUlid().toString();
        }

        List<PostResDTO> postResDTOList = new ArrayList<>();
        List<GetPostDTO> getPostDTOList = postRepository.getPostListByUserId(userId, nextIndex, pageSize);
        PageDTO pageInfo = createPageDTO(pageSize, getPostDTOList);
        for (GetPostDTO getPostDTO : getPostDTOList) {
            postResDTOList.add(getPostDTOToPostResDTO(userId, getPostDTO));
        }

        BoardListResDTO boardListResDTO = BoardListResDTO.builder()
                .postList(postResDTOList).pagingInfo(pageInfo).build();
        return boardListResDTO;
    }

    @Override
    public BoardListResDTO getFeed(String userId, String nextIndex, int pageSize) {
        if (nextIndex == null) {
            nextIndex = UlidCreator.getUlid().toString();
        }

        List<PostResDTO> postResDTOList = new ArrayList<>();
        List<GetPostDTO> getPostDTOList = postRepository.getFeedByUserId(userId, nextIndex, pageSize);
        for (GetPostDTO getPostDTO : getPostDTOList) {
            postResDTOList.add(getPostDTOToPostResDTO(userId, getPostDTO));
        }


        PageDTO pageInfo = createPageDTO(pageSize, getPostDTOList);

        BoardListResDTO feedListDTO = BoardListResDTO.builder().postList(postResDTOList).pagingInfo(pageInfo).build();
        return feedListDTO;
    }
}
