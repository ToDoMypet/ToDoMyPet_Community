package com.todomypet.communityservice.repository;

import com.todomypet.communityservice.domain.node.Reply;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends Neo4jRepository<Reply, String> {
    @Query("MATCH (reply:Reply{id:$replyId}) SET reply.deleted = true")
    void deleteReplyById(String replyId);

    @Query("MATCH (reply:Reply{id:$replyId}) RETURN reply")
    Optional<Reply> findReplyById(String replyId);

    @Query("MATCH (post:Post{id:$postId}) WITH post " +
            "MATCH (reply:Reply)-[to:TO]->(post) WHERE $nextIndex <= reply.id " +
            "WITH reply, to, post ORDER BY reply.id LIMIT $pageSize + 1 " +
            "MATCH (user:User)-[write:WRITE]->(reply) RETURN user, write, reply")
    List<Reply> getReplyListByPostId(String postId, String nextIndex, int pageSize);

    @Query("MATCH (reply:Reply{id:$replyId}) " +
            "SET reply.content = $content")
    void update(String replyId, String content);
}
