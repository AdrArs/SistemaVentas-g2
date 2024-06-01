package com.codigo.gateway_service.client;

import com.codigo.gateway_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "security-service",url = "http://localhost:8086" ,configuration = FeignConfig.class)
public interface AuthClient {
    @PostMapping("/api/authentication/validate2")
    boolean getAuth(@RequestParam("token") String token);

}
