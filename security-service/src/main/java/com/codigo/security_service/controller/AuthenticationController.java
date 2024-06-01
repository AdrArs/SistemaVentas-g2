package com.codigo.security_service.controller;

import com.codigo.security_service.aggregates.request.AuthRequest;
import com.codigo.security_service.aggregates.response.AuthResponse;
import com.codigo.security_service.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest authRequest){
        return authenticationService.authenticate(authRequest);
    }
}
