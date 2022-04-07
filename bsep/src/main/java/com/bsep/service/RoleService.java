package com.bsep.service;

import com.bsep.model.Administrator;
import com.bsep.model.Client;
import com.bsep.repository.AdministratorRepository;
import com.bsep.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private AdministratorRepository administratorRepository;
    private ClientRepository clientRepository;

    public Client getClient(String username){
        return clientRepository.findByUsername(username);
    }

    public Administrator getAdministrator(String username){
        return administratorRepository.findByUsername(username);
    }
}
