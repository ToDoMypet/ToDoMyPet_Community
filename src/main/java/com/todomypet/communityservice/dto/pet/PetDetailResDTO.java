package com.todomypet.communityservice.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
public class PetDetailResDTO {
    private String grade;
    private String name;
    private String description;
    private String portraitUrl;
    private String type;
    private String personality;
}
