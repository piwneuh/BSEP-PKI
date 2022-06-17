package com.bsep.controller;

import com.bsep.dto.security.LoginRequest;
import com.bsep.dto.security.LoginResponse;
import com.bsep.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.AuthenticationException;

@Controller
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value="/login", consumes="application/json")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws AuthenticationException {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }
}
