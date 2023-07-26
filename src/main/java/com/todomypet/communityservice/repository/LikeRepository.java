package com.todomypet.communityservice.repository;

import com.todomypet.communityservice.domain.node.User;
import com.todomypet.communityservice.domain.relationship.Like;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface LikeRepository extends Neo4jRepository<Like, Long> {

    @Query("MATCH (u:User) WITH u " +
            "MATCH (p:Post) " +
            "WHERE u.id = $userId AND p.id = $postId " +
            "CREATE (u)-[:LIKE{seq: $seq}]->(p)")
    void like(String userId, String postId, String seq);

    @Query("MATCH (u:User{id:$userId})-[like:LIKE]->(p:Post{id:$postId}) DELETE like")
    void unlike(String userId, String postId);

    @Query("MATCH (u:User) WITH u " +
            "MATCH (p:Post) " +
            "WHERE u.id = $userId AND p.id = $postId " +
            "RETURN exists((u)-[:LIKE]->(p))")
    boolean existsLikeByUserAndPost(String userId, String postId);
}
