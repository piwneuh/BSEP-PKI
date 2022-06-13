package com.bsep.service;

import com.bsep.dto.security.LoginRequest;
import com.bsep.dto.security.LoginResponse;
import com.bsep.model.Client;
import com.bsep.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class AuthService {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    public LoginResponse login(LoginRequest request) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        Client user = clientService.loadUserByUsername(request.getUsername());
        String token = jwtUtils.generateToken(user);

        return new LoginResponse(token, user);
    }

    public Client getCurrentUser() {
        return (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
