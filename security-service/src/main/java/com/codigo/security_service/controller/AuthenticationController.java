package com.codigo.security_service.controller;

import com.codigo.security_service.aggregates.request.AuthRequest;
import com.codigo.security_service.aggregates.response.AuthResponse;
import com.codigo.security_service.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest authRequest){
        return authenticationService.authenticate(authRequest);
    }

    @PostMapping("/validate")
    public AuthResponse validate(@RequestParam("token") String token){
        boolean flag = authenticationService.validate(token);
        if (!flag){
            return new AuthResponse();
        }
        return AuthResponse.builder()
                .jwt(token)
                .build();
    }

    @PostMapping("/validate2")
    public boolean validate2(@RequestParam("token") String token){
        return authenticationService.validate(token);
    }
}
