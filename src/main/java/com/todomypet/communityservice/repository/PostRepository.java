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

    @Query("MATCH (post:Post) WHERE post.id = $postId SET post.content = $content, post.imageUrl = $imgUrl")
    void updatePost(String postId, String content, String imgUrl);

    @Query("MATCH (user:User)-[:WRITE]->(post:Post) " +
            "WHERE user.id = $userId WITH user, post ORDER BY post.id " +
            "RETURN user{.profilePicUrl, .nickname} AS writer, " +
            "post{.content, .createdAt, .imageUrl, .likeCount, .replyCount} AS postInfo")
    List<GetPostDTO> getPostListByUserId(String userId);

    @Query("MATCH (user:User) WHERE user.id = $userId OR (:User{id:$userId})-[:FRIEND]-(user) WITH user " +
            "MATCH (user)-[:WRITE]->(post:Post) WHERE post.id <= $nextIndex " +
            "WITH user, post ORDER BY post.id DESC LIMIT $pageSize + 1 " +
            "RETURN user{.profilePicUrl, .nickname} AS writer, " +
            "post{.id, .content, .createdAt, .imageUrl, .likeCount, .replyCount} AS postInfo")
    List<GetPostDTO> getFeedByUserId(String userId, String nextIndex, int pageSize);
}
