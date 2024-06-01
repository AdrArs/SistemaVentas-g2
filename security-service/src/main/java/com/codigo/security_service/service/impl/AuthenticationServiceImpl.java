package com.codigo.security_service.service.impl;

import com.codigo.security_service.aggregates.request.AuthRequest;
import com.codigo.security_service.aggregates.response.AuthResponse;
import com.codigo.security_service.dao.PersonRepository;
import com.codigo.security_service.service.AuthenticationService;
import com.codigo.security_service.service.JWTService;
import com.codigo.security_service.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PersonService personService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    @Override
    public ResponseEntity<AuthResponse> authenticate(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getCorreo(),authRequest.getClave()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse());
        }
        final UserDetails userDetails = personService.userDetailService().loadUserByUsername(authRequest.getCorreo());

        var jwt = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}
