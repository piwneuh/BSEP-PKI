package com.bsep.repository;

import com.bsep.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findClientByUsername(String username);

    Client findClientByUsernameAndPassword(String username, String password);
}
