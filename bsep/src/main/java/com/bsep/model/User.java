package com.bsep.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    private Long id;

    private String name;

    private String surname;

    private String email;


    public UserData() { }


    public UserData(String name, String surname, String email){

        this.name=name;
        this.surname=surname;
        this.email=email;

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
