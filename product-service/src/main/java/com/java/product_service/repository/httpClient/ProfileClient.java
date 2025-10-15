package com.java.product_service.repository.httpClient;

import com.java.common_dto.ProfileGetResponse;
import com.java.product_service.config.security.AuthenticationRequestInterceptor;
import com.java.product_service.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @GetMapping("/auth/internal/users/{userId}")
    ApiResponse<ProfileGetResponse> getProfile(@PathVariable String userId);
}

