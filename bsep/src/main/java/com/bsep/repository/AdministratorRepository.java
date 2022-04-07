package com.bsep.repository;

import com.bsep.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

    public Administrator findByUsername(String username);

    public Administrator findByUsernameAndPassword(String username, String password);
}
