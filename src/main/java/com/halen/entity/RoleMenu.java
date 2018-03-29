package com.halen.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_roleMenu")
public class RoleMenu {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "menuId")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;
}
