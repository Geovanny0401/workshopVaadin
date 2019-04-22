package com.gmail.geovanny.spring.backend.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Data
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @NotNull
    @Size(min=3, max=50)
    private String firstName;

    @NotNull
    @Size(min=3, max=50)
    private String lastName;

    @Email
    private String contactEmail;

    @NotNull
    @Size(min=8, max=100)
    private String password;

    private Boolean blocked;

    private String country;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @ManyToOne
    private Role mainRole;


}
