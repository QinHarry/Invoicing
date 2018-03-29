package com.halen.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_menu")
public class Menu {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 50)
    private String name;

    @Column(length = 200)
    private String url;

    private Integer state;  // 1 means root node, 0 means leaf node

    @Column(length = 100)
    private String icon;

    private Integer pId; //parent node id
}
