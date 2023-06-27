package com.todomypet.communityservice.repository;

import com.todomypet.communityservice.domain.relationship.Write;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WriteRepository extends Neo4jRepository<Write, Long> {
    @Query("MATCH (post:Post{id:$postId}) WITH post MATCH (user:User{id:$userId}) " +
           "CREATE (post)<-[:WRITE]-(user)")
    void setWriteRelationship(String userId, String postId);

    @Query("MATCH (reply:Reply{id:$replyId}) WITH reply MATCH (user:User{id:$userId}) CREATE (reply)<-[:WRITE]-(user)")
    void setWriteBetweenReplyAndUser(String userId, String replyId);
}
