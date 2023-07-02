package com.todomypet.communityservice.domain.node;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDateTime;

@Node("User")
@Getter
public class User {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String id;

    @Property("email")
    private String email;

    @Property("password")
    private String password;

    @Property("nickname")
    private String nickname;

    @Property("bio")
    private String bio;

    @Property("profilePicUrl")
    private String profilePicUrl;

    @Property("oauthKey")
    private String oauth_key;

//    @Property("personal_code")
//    private String personalCode;

//    private Integer petCount;
//    private Integer achCount;
//    private Integer attendCount;
//    private Integer petEvolveCount;
//    private Integer petCompleteCount;
//    private Integer attendContinueCount;

    @Property("protected")
    private Boolean Protected;

    @Property("cratedAt")
    private LocalDateTime createdAt;

    @Property("deletedAt")
    private LocalDateTime deletedAt;

    @Property("refreshToken")
    private String refreshToken;

    @Property("deleted")
    private Boolean deleted;
}
