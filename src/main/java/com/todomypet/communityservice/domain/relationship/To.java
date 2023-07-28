package com.todomypet.communityservice.domain.relationship;

import com.todomypet.communityservice.domain.node.Post;
import com.todomypet.communityservice.domain.node.Reply;
import lombok.Getter;
import org.mapstruct.MappingTarget;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Getter
public class To {
    @RelationshipId
    private Long id;

    @TargetNode
    private Post post;
}
