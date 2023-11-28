package com.todomypet.communityservice.domain.node;

import com.github.f4b6a3.ulid.UlidCreator;
import com.github.f4b6a3.ulid.UlidFactory;
import com.todomypet.communityservice.domain.relationship.Like;
import com.todomypet.communityservice.domain.relationship.Write;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Node("Post")
@Builder
@Getter
public class Post {
    @Id
    private String id;

    @Property("content")
    private String content;

    @Property("createdAt")
    private LocalDateTime createdAt;

    @Property("deleted")
    private Boolean deleted;

    @Property("likeCount")
    private Integer likeCount;

    @Property("replyCount")
    private Integer replyCount;

    @Property("imageUrl")
    private List<String> imageUrl;

    @Property("petId")
    private String petId;

    @Property("backgroundId")
    private String backgroundId;

    @Relationship(type = "WRITE", direction = Relationship.Direction.INCOMING)
    private Write write;

    @Relationship(type = "LIKE", direction = Relationship.Direction.INCOMING)
    private Set<Like> likes;

    @Relationship(type = "TO", direction = Relationship.Direction.INCOMING)
    private Set<Reply> reply;
}
