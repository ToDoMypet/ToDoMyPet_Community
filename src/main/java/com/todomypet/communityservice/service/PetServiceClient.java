package com.todomypet.communityservice.service;

import com.todomypet.communityservice.dto.pet.PetDetailResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="pet-service", url = "${feign.pet.url}")
public interface PetServiceClient {

    @GetMapping(value = "/adopted-pet-list/my-pet-info/detail/{seq}", consumes = "application/json")
    PetDetailResDTO getPetDetailInfo(@RequestHeader String userId, @PathVariable String seq);
}
