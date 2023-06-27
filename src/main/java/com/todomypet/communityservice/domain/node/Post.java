package com.todomypet.communityservice.domain.node;

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
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String id;

    @Property("content")
    private String content;

    @Property("created_at")
    private LocalDateTime createdAt;

    @Property("deleted")
    private Boolean deleted;

    @Property("like_count")
    private Integer likeCount;

    @Property("reply_count")
    private Integer replyCount;

    @Property("image_url")
    private List<String> imageUrl;

    @Relationship(type = "WRITE", direction = Relationship.Direction.INCOMING)
    private Write write;

    @Relationship(type = "LIKE", direction = Relationship.Direction.INCOMING)
    private Set<Like> likes;

    @Relationship(type = "TO", direction = Relationship.Direction.INCOMING)
    private Set<Reply> reply;
}
