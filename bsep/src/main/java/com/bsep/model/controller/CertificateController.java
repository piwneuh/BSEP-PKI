package com.bsep.model.controller;

import com.bsep.model.Administrator;
import com.bsep.model.Client;
import com.bsep.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CertificateController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("/getUser/{username}")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUser(@PathVariable String username){
        Client client = roleService.getClient(username);
        Administrator admin = roleService.getAdministrator(username);
        if(client != null)
            return new ResponseEntity<String>(client.getName() + "+" + client.getRole(), HttpStatus.FOUND);
        if(admin != null)
            return new ResponseEntity<String>(admin.getName() + "+" + admin.getRole(), HttpStatus.FOUND);
    }
}
