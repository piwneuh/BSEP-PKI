package com.bsep.service;

import com.bsep.model.Client;
import com.bsep.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientService implements UserDetailsService {

    @Autowired
    private ClientRepository repo;

    public Client findClientByUsername(String username) {
        return this.repo.findClientByUsername(username);
    }

    @Override
    public Client loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.repo.findClientByUsername(username);
    }
}
