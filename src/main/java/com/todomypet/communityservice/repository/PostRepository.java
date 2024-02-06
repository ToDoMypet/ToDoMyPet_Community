package com.todomypet.communityservice.repository;

import com.todomypet.communityservice.domain.node.Post;
import com.todomypet.communityservice.dto.post.GetPostDTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends Neo4jRepository<Post, String> {
    @Query("MATCH (post:Post) WHERE post.id = $postId RETURN post")
    Optional<Post> findPostById(String postId);

    @Query("MATCH (post:Post) WHERE post.id = $postId SET post.deleted = true")
    void deletePostById(String postId);

    @Query("MATCH (post:Post) WHERE post.id = $postId SET post.likeCount = post.likeCount + 1")
    void increaseLikeCountById(String postId);

    @Query("MATCH (post:Post) WHERE post.id = $postId SET post.likeCount = post.likeCount - 1")
    void decreaseLikeCountById(String postId);

    @Query("MATCH (post:Post) WHERE post.id = $postId SET post.replyCount = post.replyCount + 1")
    void increaseReplyCountById(String postId);

    @Query("MATCH (post:Post) WHERE post.id = $postId SET post.replyCount = post.replyCount - 1")
    void decreaseReplyCountById(String postId);

    @Query("MATCH (post:Post) WHERE post.id = $postId SET post.content = $content, post.imageUrl = $imgUrl, " +
            "post.petId = $petId, post.backgroundId = $backgroundId")
    void updatePost(String postId, String content, List<String> imgUrl, String petId, String backgroundId);

    @Query("MATCH (user:User{id:$userId}) WITH user " +
            "MATCH (user)-[:WRITE]->(post:Post) WHERE post.id <= $nextIndex AND post.deleted = false " +
            "WITH user,post ORDER BY post.id DESC LIMIT $pageSize + 1 " +
            "RETURN user{.id, .profilePicUrl, .nickname} AS writer, " +
            "post{.id, .content, .createdAt, .imageUrl, .likeCount, .replyCount, .petId, .backgroundId} AS postInfo")
    List<GetPostDTO> getPostListByUserId(String userId, String nextIndex, int pageSize);

    @Query("MATCH (user:User) WHERE user.id = $userId OR (:User{id:$userId})-[:FRIEND]-(user) WITH user " +
            "MATCH (user)-[:WRITE]->(post:Post) WHERE post.id <= $nextIndex AND post.deleted = false " +
            "WITH user, post ORDER BY post.id DESC LIMIT $pageSize + 1 " +
            "RETURN user{.id, .profilePicUrl, .nickname} AS writer, " +
            "post{.id, .content, .createdAt, .imageUrl, .likeCount, .replyCount, .petId, .backgroundId} AS postInfo")
    List<GetPostDTO> getFeedByUserId(String userId, String nextIndex, int pageSize);

    @Query("MATCH (post:Post{id:$postId}) WITH post MATCH (user:User)-[:WRITE]->(post) " +
            "RETURN user{.id, .profilePicUrl, .nickname} AS writer, " +
            "post{.id, .content, .createdAt, .imageUrl, .likeCount, .replyCount, .petId, .backgroundId} AS postInfo")
    GetPostDTO getPostById(String postId);

    @Query("MATCH (post:Post) WITH post MATCH (user:User)-[:WRITE]->(post) RETURN user{.id, .profilePicUrl, .nickname} AS writer, " +
            "post{.id, .content, .createdAt, .imageUrl, .likeCount, .replyCount, .petId, .backgroundId} AS postInfo")
    List<GetPostDTO> findAllPost();
}
