package com.todomypet.communityservice.controller;

import com.todomypet.communityservice.dto.*;
import com.todomypet.communityservice.dto.post.*;
import com.todomypet.communityservice.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Board Controller")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/admin/get-all-posts")
    public SuccessResDTO<AdminGetAllPostDTO> adminGetAllPosts() {
        AdminGetAllPostDTO response = boardService.getAllPost();
        return new SuccessResDTO<AdminGetAllPostDTO>(response);
    }

    @Operation(summary = "글 작성", description = "글을 작성하여 발행합니다.")
    @PostMapping("")
    public SuccessResDTO<WritePostResDTO> WritePost(@RequestHeader String userId,
                                                    @RequestBody WritePostReqDTO writePostReqDTO) {
        String responseId = boardService.post(userId, writePostReqDTO);
        WritePostResDTO writePostResDTO = WritePostResDTO.builder().id(responseId).build();
        return new SuccessResDTO<WritePostResDTO>(writePostResDTO);
    }

    @Operation(summary = "피드 조회", description = "피드를 조회합니다.")
    @GetMapping("")
    public SuccessResDTO<BoardListResDTO> getFeed(@RequestHeader String userId,
                                                  @RequestParam(required = false) String nextIndex,
                                                  @RequestParam(required = false, defaultValue = "20") int pageSize) {
        BoardListResDTO response = boardService.getFeed(userId, nextIndex, pageSize);
        return new SuccessResDTO<BoardListResDTO>(response);
    }

    @Operation(summary = "글 자세히 보기", description = "특정 글을 조회합니다.")
    @GetMapping("/{postId}")
    public SuccessResDTO<PostResDTO> getPostDetail(@RequestHeader String userId, @PathVariable String postId) {
        PostResDTO response = boardService.getPostDetailById(userId, postId);
        return new SuccessResDTO<PostResDTO>(response);
    }

    @Operation(summary = "글 삭제", description = "특정 글을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public SuccessResDTO<Void> deletePost(@RequestHeader String userId, @PathVariable String postId) {
        boardService.deletePost(userId, postId);
        return new SuccessResDTO<>(null);
    }

    @Operation(summary = "글 수정", description = "글을 수정합니다. 글 내용과 첨부 이미지, 펫과 펫룸 이미지를 수정할 수 있습니다.")
    @PutMapping("/{postId}")
    public SuccessResDTO<PostUpdateResDTO> updatePost(@RequestHeader String userId, @PathVariable String postId,
                                                      @RequestBody PostUpdateReqDTO postUpdateReqDTO) {
        boardService.updatePost(userId, postId, postUpdateReqDTO);
        PostUpdateResDTO response = PostUpdateResDTO.builder().id(postId).build();
        return new SuccessResDTO<PostUpdateResDTO>(response);
    }

    @Operation(summary = "내가 쓴 글 조회", description = "로그인된 사용자가 작성한 글 리스트를 조회합니다.")
    @GetMapping("/my-post-list")
    public SuccessResDTO<BoardListResDTO> getMyPostList(@RequestHeader String userId,
                                                        @RequestParam(required = false) String nextIndex,
                                                        @RequestParam(required = false, defaultValue = "20") int pageSize) {
        BoardListResDTO response = boardService.getMyPostList(userId, nextIndex, pageSize);
        return new SuccessResDTO<BoardListResDTO>(response);
    }
}
