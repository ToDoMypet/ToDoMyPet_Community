package com.todomypet.communityservice.repository;

import com.todomypet.communityservice.domain.relationship.Write;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WriteRepository extends Neo4jRepository<Write, Long> {
    @Query("MATCH (post:Post{id:$postId}) WITH post MATCH (user:User{id:$userId}) " +
           "CREATE (post)<-[:WRITE{seq: $seq}]-(user)")
    void setWriteRelationship(String userId, String postId, String seq);

    @Query("MATCH (reply:Reply{id:$replyId}) WITH reply MATCH (user:User{id:$userId}) CREATE (reply)<-[:WRITE]-(user)")
    void setWriteBetweenReplyAndUser(String userId, String replyId);

    @Query("MATCH (u:User{id:$userId}) WITH u MATCH (p:Post{id:$postId}) RETURN exists((u)-[:WRITE]->(p))")
    boolean existsWriteBetweenUserAndPost(String userId, String postId);

    @Query("MATCH (u:User{id:$userId}) WITH u MATCH (r:Reply{id:$replyId}) RETURN exists((u)-[:WRITE]->(r))")
    boolean existsWriteBetweenUserAndReply(String userId, String replyId);
}
