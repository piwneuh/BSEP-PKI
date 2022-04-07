package com.bsep.service;

import com.bsep.model.Administrator;
import com.bsep.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService {

    @Autowired
    private AdministratorRepository repo;

    public Administrator findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public Administrator login(String username, String password) {
        return repo.findByUsernameAndPassword(username, password);
    }
}
