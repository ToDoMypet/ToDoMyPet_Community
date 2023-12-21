package com.todomypet.communityservice.service;

import com.todomypet.communityservice.dto.pet.PetDetailResDTO;
import com.todomypet.communityservice.dto.post.FeignClientResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="pet-service", url = "${feign.pet.url}")
public interface PetServiceClient {

    @GetMapping(value = "/pet/adopted-pet-list/my-pet-info/detail/{seq}", consumes = "application/json")
    FeignClientResDTO<PetDetailResDTO> getPetDetailInfo(@RequestHeader String userId, @PathVariable String seq);

    @GetMapping(value = "/pet/background/{backgroundId}")
    FeignClientResDTO<String> getBackgroundUrlById(@PathVariable String backgroundId);
}
