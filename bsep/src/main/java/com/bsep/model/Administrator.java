package com.bsep.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Administrator{
    @Id
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private UserRole role;

    public Administrator(){
    }

}
