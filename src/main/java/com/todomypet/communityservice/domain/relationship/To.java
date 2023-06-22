package com.todomypet.communityservice.domain.relationship;

import com.todomypet.communityservice.domain.node.Reply;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class To {
    @RelationshipId
    private Long id;

    @TargetNode
    private Reply reply;
}
