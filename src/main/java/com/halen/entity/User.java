package com.halen.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 50)
    @NotEmpty(message = "Please input username")
    private String username;

    @Column(length = 50)
    @NotEmpty(message = "Please input password")
    private String password;

    @Column(length = 50)
    private String trueName;

    @Transient
    private String roles;

    @Column(length = 1000)
    private String remarks;
}
