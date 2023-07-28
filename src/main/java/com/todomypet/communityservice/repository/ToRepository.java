package com.todomypet.communityservice.repository;

import com.todomypet.communityservice.domain.relationship.To;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ToRepository extends Neo4jRepository<To, Long> {

    @Query("MATCH (reply:Reply{id:$replyId}) WITH reply " +
            "MATCH (post:Post{id:$postId}) " +
            "CREATE (reply)-[:TO]->(post)")
    void setToBetweenPostAndReply(String postId, String replyId);

}
