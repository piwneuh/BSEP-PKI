package com.bsep.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Client {

    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String email;

    @Column
    private UserRole role;

    public Client() { }

    public Client(String name, String surname, String email){
        this.name=name;
        this.surname=surname;
        this.email=email;
    }
}
