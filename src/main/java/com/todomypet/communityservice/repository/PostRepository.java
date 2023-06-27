package com.todomypet.communityservice.repository;

import com.todomypet.communityservice.domain.node.Post;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;

public interface PostRepository extends Neo4jRepository<Post, String> {
    @Query("MATCH (post:Post) WHERE post.id = $postId RETURN post")
    Optional<Post> findPostById(String postId);

    @Query("MATCH (post:Post) WHERE post.id = $postId SET post.deleted = true")
    void deletePostById(String postId);

    @Query("MATCH (post:Post) WHERE post.id = $postId SET post.like_count = post.like_count + 1")
    void increaseLikeCountById(String postId);

    @Query("MATCH (post:Post) WHERE post.id = $postId SET post.like_count = post.like_count - 1")
    void decreaseLikeCountById(String postId);

    @Query("MATCH (post:Post) WHERE post.id = $postId SET post.reply_count = post.reply_count + 1")
    void increaseReplyCountById(String postId);

    @Query("MATCH (post:Post) WHERE post.id = $postId SET post.reply_count = post.reply_count - 1")
    void decreaseReplyCountById(String postId);

    @Query("MATCH (post:Post) WHERE post.id = $postId SET post.content = $content, post.image_url = $imgUrl")
    void updatePost(String postId, String content, String imgUrl);
}
