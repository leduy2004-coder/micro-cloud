package com.java.auth_service.repository.feignClient;


import com.java.auth_service.config.security.AuthenticationRequestInterceptor;
import com.java.auth_service.dto.ApiResponse;
import com.java.common_dto.ProfileCreationRequest;
import com.java.common_dto.ProfileCreationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @PostMapping(value = "/internal/users")
    ApiResponse<ProfileCreationResponse> createProfile(@RequestBody ProfileCreationRequest request);
}