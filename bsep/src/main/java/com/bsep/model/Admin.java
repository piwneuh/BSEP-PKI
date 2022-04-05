package com.bsep.model;

import javax.persistence.Entity;

@Entity
public class Admin{
    private Long id;

    private String username;

    private String password;

    public Administrator(){


    }

    public String getUsername(){

        return this.username;
    }

    public Long getId(){

        return id;
    }
}
