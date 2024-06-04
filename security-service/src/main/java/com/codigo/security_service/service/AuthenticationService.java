package com.codigo.security_service.service;

import com.codigo.security_service.aggregates.request.AuthRequest;
import com.codigo.security_service.aggregates.response.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<AuthResponse> authenticate(AuthRequest authRequest);
    boolean validate(String token);
}
