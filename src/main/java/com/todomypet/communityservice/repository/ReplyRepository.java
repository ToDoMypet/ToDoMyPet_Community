package com.todomypet.communityservice.repository;

import com.todomypet.communityservice.domain.node.Reply;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplyRepository extends Neo4jRepository<Reply, String> {
    @Query("MATCH (reply:Reply{id:$replyId}) SET reply.deleted = true")
    void deleteReplyById(String replyId);

    @Query("MATCH (reply:Reply{id:$replyId}) RETURN reply")
    Optional<Reply> findReplyById(String replyId);
}
