package com.bsep.controller;

import com.bsep.model.Administrator;
import com.bsep.service.AdministratorService;
import com.bsep.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/login/{username}/{password}")
    public ResponseEntity<?> login(@PathVariable("username") String username, @PathVariable("password") String password) {
        Administrator administrator = administratorService.login(username, password);
        if (administrator != null) {
            return ResponseEntity.ok(administrator);
        } else {
            return ResponseEntity.ok(null);
        }
    }


}
