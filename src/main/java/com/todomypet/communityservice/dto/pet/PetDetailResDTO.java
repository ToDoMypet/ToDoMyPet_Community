package com.todomypet.communityservice.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
public class PetDetailResDTO {
    private String grade;
    private String name;
    private String description;
    private String imageUrl;
    private String type;
    private String personality;
}
