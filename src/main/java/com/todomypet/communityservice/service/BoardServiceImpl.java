package com.todomypet.communityservice.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.todomypet.communityservice.domain.node.Post;
import com.todomypet.communityservice.domain.node.User;
import com.todomypet.communityservice.dto.ect.PageDTO;
import com.todomypet.communityservice.dto.ect.ReportDTO;
import com.todomypet.communityservice.dto.ect.ReportType;
import com.todomypet.communityservice.dto.pet.PetDetailResDTO;
import com.todomypet.communityservice.dto.post.*;
import com.todomypet.communityservice.dto.user.GetWriterDTO;
import com.todomypet.communityservice.dto.user.WriterResDTO;
import com.todomypet.communityservice.exception.CustomException;
import com.todomypet.communityservice.exception.ErrorCode;
import com.todomypet.communityservice.repository.LikeRepository;
import com.todomypet.communityservice.repository.PostRepository;
import com.todomypet.communityservice.repository.UserRepository;
import com.todomypet.communityservice.repository.WriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final MailService mailService;

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
                .petId(getPostDTO.getPostInfo().getPetId())
                .petName(petDetailResDTO.getName())
                .petGrade(petDetailResDTO.getGrade())
                .petImageUrl(petDetailResDTO.getImageUrl())
                .backgroundId(getPostDTO.getPostInfo().getBackgroundId())
                .backgroundImageUrl(backgroundImageUrl)
                .isLiked(likeRepository.existsLikeByUserAndPost(userId, postInfo.getId())).build();
        GetWriterDTO writer = getPostDTO.getWriter();
        WriterResDTO writerResDTO = WriterResDTO.builder().id(writer.getId()).nickname(writer.getNickname())
                .profilePicUrl(writer.getProfilePicUrl()).deleted(writer.isDeleted())
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
        log.info(">>> 글 작성 API 진입: " + userId);
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
    @Transactional
    public void deletePost(String userId, String postId) {
        Post post = postRepository.findPostById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXIST));
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
        log.info(">>> 글 수정 API 진입: " + userId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXIST));
        if (post.getDeleted()) {
            throw new CustomException(ErrorCode.DELETED_POST);
        }
        User user = userRepository.findWriterByPostId(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        if (!user.getId().equals(userId)) {
            log.error(">>> 수정 userId 불일치 (진입 Id)" + userId + " (글 작성 Id)" + user.getId());
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        List<String> imgList = new ArrayList<>();
        if (!postUpdateReqDTO.getImageUrls().isEmpty()) {
            for (String fileString : postUpdateReqDTO.getImageUrls()) {
                imgList.add(s3Uploader.upload(fileString));
            }
        }

        postRepository.updatePost(postId, postUpdateReqDTO.getContent(), imgList,
                postUpdateReqDTO.getPetId(), postUpdateReqDTO.getBackgroundId());
    }

    @Override
    public BoardListResDTO getMyPostList(String userId, String nextIndex, int pageSize) {
        return getPostListByUser(userId, nextIndex, pageSize);
    }

    @Override
    public BoardListResDTO getFeed(String userId, String nextIndex, int pageSize) {
        log.info(">>> 피드 조회 API 진입: " + userId);
        if (nextIndex == null) {
            nextIndex = UlidCreator.getUlid().toString();
        }

        log.info(nextIndex);

        List<PostResDTO> postResDTOList = new ArrayList<>();
        List<GetPostDTO> getPostDTOList = postRepository.getFeedByUserId(userId, nextIndex, pageSize);
        for (GetPostDTO getPostDTO : getPostDTOList) {
            postResDTOList.add(getPostDTOToPostResDTO(userId, getPostDTO));
        }

        PageDTO pageInfo = createPageDTO(pageSize, getPostDTOList);

        BoardListResDTO feedListDTO = BoardListResDTO.builder().postList(postResDTOList).pagingInfo(pageInfo).build();
        return feedListDTO;
    }

    @Override
    public PostResDTO getPostDetailById(String userId, String postId) {
        GetPostDTO getPostDTO = postRepository.getPostById(postId);
        PostResDTO postResDTO = getPostDTOToPostResDTO(userId, getPostDTO);
        return postResDTO;
    }

    @Override
    public AdminGetAllPostDTO getAllPost() {
        List<PostResDTO> postResDTOList = new ArrayList<>();
        List<GetPostDTO> posts = postRepository.findAllPost();
        for (GetPostDTO getPostDTO : posts) {
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
                    .petId(getPostDTO.getPostInfo().getPetId())
                    .petName(petDetailResDTO.getName())
                    .petGrade(petDetailResDTO.getGrade())
                    .petImageUrl(petDetailResDTO.getImageUrl())
                    .backgroundId(getPostDTO.getPostInfo().getBackgroundId())
                    .backgroundImageUrl(backgroundImageUrl)
                    .build();
            WriterResDTO writerResDTO = WriterResDTO.builder().nickname(getPostDTO.getWriter().getNickname())
                    .profilePicUrl(getPostDTO.getWriter().getProfilePicUrl()).build();

            PostResDTO postResDTO = PostResDTO.builder().postInfo(postInfoResDTO).writer(writerResDTO).build();
            postResDTOList.add(postResDTO);
        }

        AdminGetAllPostDTO response = AdminGetAllPostDTO.builder().postList(postResDTOList).build();
        return response;
    }

    @Override
    @Transactional
    public void reportPost(String userId, String postId) {
        GetPostDTO post = postRepository.getPostById(postId);
        GetPostInfoDTO postInfo = post.getPostInfo();
        GetWriterDTO writer = post.getWriter();
        ReportDTO reportInfo = ReportDTO.builder().reportType(ReportType.POST).reportedId(postId).reportedContent(postInfo.getContent())
                .reportedUri(postInfo.getImageUrl().toString()).reporterId(userId)
                .writerId(writer.getId())
                .writerNickname(writer.getNickname()).build();
        try {
            mailService.sendReportMail(reportInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.MAIL_SEND_FAIL);
        }

    }

    @Override
    public String deletePostByAdminAccount(String postId) {
        postRepository.deletePostById(postId);
        return postId;
    }

    @Override
    public BoardListResDTO getPostListByUserId(String userId, String targetId, String nextIndex, int pageSize) {
        return getPostListByUser(targetId, nextIndex, pageSize);
    }

    public BoardListResDTO getPostListByUser(String userId, String nextIndex, int pageSize) {
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
}
