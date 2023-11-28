package com.todomypet.communityservice.domain.node;

import lombok.Getter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("background")
@Getter
public class Background {
    @Id
    private String id;
    @Property("backgroundImageUrl")
    private String backgroundImageUrl;
}
