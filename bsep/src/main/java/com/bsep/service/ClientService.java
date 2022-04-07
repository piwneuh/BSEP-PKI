package com.bsep.service;

import com.bsep.model.Client;
import com.bsep.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repo;

    public Client findClientByUsername(String username) {
        return this.repo.findClientByUsername(username);
    }

    public Client login(String username, String password) {
        return this.repo.findClientByUsernameAndPassword(username, password);
    }
}
