package com.halen.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_userRole")
public class UserRole {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;
}
