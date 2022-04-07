package com.bsep.controller;

import com.bsep.model.Administrator;
import com.bsep.service.AdministratorService;
import com.bsep.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @PostMapping("/login/{username}/{password}")
    public Object login(@PathVariable("username") String username, @PathVariable("password") String password) {
        Administrator administrator = administratorService.login(username, password);
        if (administrator != null) {
            return administrator;
        } else {
            return clientService.login(username, password);
        }
    }


}
