package com.todomypet.communityservice.repository;

import com.todomypet.communityservice.domain.node.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {
    @Query("MATCH (user:User) WHERE user.id = $userId RETURN user")
    Optional<User> findUserById(String userId);

    @Query("MATCH (user:User)-[:WRITE]->(post:Post{id:$postId}) RETURN user")
    Optional<User> findWriterByPostId(String postId);

    @Query("MATCH (reply:Reply{id:$replyId})<-[:WRITE]-(user:User) RETURN user")
    Optional<User> findWriterByReplyId(String replyId);
}
