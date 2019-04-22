package com.gmail.geovanny.spring.backend.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRole;

    private String name;

    private Boolean module1Authorized;

    private Boolean accessModule2;

    @Override
    public String toString() { return name;}


}
