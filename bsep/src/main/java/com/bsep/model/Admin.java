package com.bsep.model;

import javax.persistence.Entity;

@Entity
public class Admin extends User{

    private static final String role = "ADMIN";

    public Admin() {
    }

    public Admin(String email, String password, String fullName) {
        super(email, password, fullName);
    }
}
