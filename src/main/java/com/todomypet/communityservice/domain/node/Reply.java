package com.todomypet.communityservice.domain.node;

import com.todomypet.communityservice.domain.relationship.To;
import com.todomypet.communityservice.domain.relationship.Write;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDateTime;

@Node("Reply")
@Builder @Getter
public class Reply {
    @Id
    private String id;

    @Property("content")
    private String content;

    @Property("createdAt")
    private LocalDateTime createdAt;

    @Property("deleted")
    private Boolean deleted;

    @Relationship(type = "WRITE", direction = Relationship.Direction.INCOMING)
    private User writer;

    @Relationship(type = "TO", direction = Relationship.Direction.OUTGOING)
    private To to;
}